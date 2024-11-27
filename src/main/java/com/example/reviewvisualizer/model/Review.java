package com.example.reviewvisualizer.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Data
@Table(name = "Reviews")
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

  @Column(nullable = false, name = "ReviewTime")
  private LocalDateTime reviewTime;

  @Min(1)
  @Max(100)
  @Column(nullable = false, name = "TeachingQuality")
  private int teachingQuality;

  @Min(1)
  @Max(100)
  @Column(nullable = false, name = "StudentsSupport")
  private int studentsSupport;

  @Min(1)
  @Max(100)
  @Column(nullable = false, name = "Communication")
  private int communication;

  @Min(1)
  @Max(100)
  @Column(nullable = false, name = "Overall")
  private double overall;

  @ManyToOne
  private Teacher teacher;
}
