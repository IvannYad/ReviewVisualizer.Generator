package com.example.reviewvisualizer.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import com.example.reviewvisualizer.dto.CreateReviewDto;
import com.example.reviewvisualizer.service.QueueService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
@Data
@Entity
@Table(name = "Reviewers")
public class Reviewer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private Integer id;

  @NotBlank
  @Column(name = "Name")
  private String name;

  @Min(100)
  @Max(100000)
  @Column(name = "ReviewGenerationFrequencyMiliseconds")
  private Integer reviewGenerationFrequencyMiliseconds;

  @Min(1)
  @Max(100)
  @Column(name = "TeachingQualityMinGrade")
  private Integer teachingQualityMinGrade;

  @Min(1)
  @Max(100)
  @Column(name = "TeachingQualityMaxGrade")
  private Integer teachingQualityMaxGrade;

  @Min(1)
  @Max(100)
  @Column(name = "StudentsSupportMinGrade")
  private Integer studentsSupportMinGrade;

  @Min(1)
  @Max(100)
  @Column(name = "StudentsSupportMaxGrade")
  private Integer studentsSupportMaxGrade;

  @Min(1)
  @Max(100)
  @Column(name = "CommunicationMinGrade")
  private Integer communicationMinGrade;

  @Min(1)
  @Max(100)
  @Column(name = "CommunicationMaxGrade")
  private Integer communicationMaxGrade;

  @Column(name = "IsStopped")
  private boolean isStopped;

  @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
  @JoinTable(
          name = "ReviewerTeacher",
          joinColumns = @JoinColumn(name = "ReviewersId"),
          inverseJoinColumns = @JoinColumn(name = "TeachersId"))
  private List<Teacher> teachers;

  @Transient
  private Consumer<Reviewer> threadCompletedListener;

  public void generateReview(QueueService queue, Logger logger) {
    if (teachers == null || teachers.isEmpty()) {
      return;
    }

    Random random = new Random();
    try {
      while (!isStopped) {
        Teacher randomTeacher = teachers.get(random.nextInt(teachers.size()));

        CreateReviewDto review = new CreateReviewDto();
        review.setReviewTime(LocalDateTime.now());
        review.setTeachingQuality(
            random.nextInt(teachingQualityMaxGrade - teachingQualityMinGrade + 1) + teachingQualityMinGrade);
        review.setStudentsSupport(
            random.nextInt(studentsSupportMaxGrade - studentsSupportMinGrade + 1) + studentsSupportMinGrade);
        review.setCommunication(
            random.nextInt(communicationMaxGrade - communicationMinGrade + 1) + communicationMinGrade);
        review.setOverall(
            (double) (review.getTeachingQuality() + review.getStudentsSupport() + review.getCommunication()) / 3);
        review.setTeacherId(randomTeacher.getId());

        if (isStopped) {
          break;
        }

        queue.addReview(review);
        logger.info("[Reviewer] Review for {} {} added [Reviewer: {}]", randomTeacher.getFirstName(),
            randomTeacher.getLastName(), name);

        Thread.sleep(reviewGenerationFrequencyMiliseconds);
      }
    } catch (InterruptedException e) {
      logger.info("[Reviewer] Reviewer {} is Interrupted", name);
      isStopped = true;
    }

    if (threadCompletedListener != null) {
      threadCompletedListener.accept(this);
    }
  }

  public record ThreadCompletedEvent(Reviewer reviewer) {
  }
}
