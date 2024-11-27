package com.example.reviewvisualizer.service;

import static org.hibernate.Hibernate.contains;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.example.reviewvisualizer.dto.CreateReviewerDto;
import com.example.reviewvisualizer.mapper.ReviewerMapper;
import com.example.reviewvisualizer.model.Reviewer;
import com.example.reviewvisualizer.model.Teacher;
import com.example.reviewvisualizer.repository.ReviewerRepository;
import com.example.reviewvisualizer.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewerService {
  private final ReviewerMapper reviewerMapper;
  private final ReviewerRepository reviewerRepository;
  public final GeneratorHostService generatorHostService;
  public final TeacherRepository teacherRepository;


  public List<Reviewer> findAllWithTeachers(){
    return reviewerRepository.findAll();
  }

  public List<Reviewer> findAll() {
    return reviewerRepository.findAll();
  }

  public boolean createReviewer(CreateReviewerDto createReviewerDto) {
    var reviewer = reviewerMapper.toModel(createReviewerDto);
    reviewer.setTeachers(new ArrayList<>());

    boolean isCreated = generatorHostService.createReviewer(reviewer);

    if (isCreated) {
      reviewerRepository.save(reviewer);
    }

    return isCreated;
  }

  public HttpStatus deleteReviewer(Integer reviewerId) {
    if (generatorHostService.deleteReviewer(findById(reviewerId))) {
      reviewerRepository.deleteById(reviewerId);
      return HttpStatus.OK;
    }
    return HttpStatus.BAD_REQUEST;
  }

  public Boolean stopReviewerById(Integer reviewerId) {
    Reviewer reviewer = findById(reviewerId);

    boolean success = generatorHostService.stopReviewer(reviewerId);

    if (success) {
      reviewer.setStopped(true);
      reviewerRepository.save(reviewer);
    }
    return success;
  }

  public Boolean startReviewerById(Integer reviewerId) {
    var reviewer = findById(reviewerId);

    boolean success = generatorHostService.startReviewer(reviewerId);

    if (success) {
      reviewer.setStopped(false);
      reviewerRepository.save(reviewer);
    }
    return success;
  }

  public List<Teacher> addTeachers(Integer reviewerId, List<Integer> teacherIds) {
    var reviewer = findById(reviewerId);

    if (Objects.isNull(reviewer.getTeachers())) {
      reviewer.setTeachers(new ArrayList<>());
    }

    List<Teacher> newTeachers = teacherRepository.findAllById(teacherIds);

    reviewer.getTeachers().addAll(newTeachers);
    reviewerRepository.save(reviewer);

    generatorHostService.addTeacher(reviewerId, newTeachers);

    log.info("Teachers [{}] are added to reviewer {}",
        newTeachers.stream()
            .map(t -> t.getFirstName() + " " + t.getLastName())
            .collect(Collectors.joining(", ")),
        reviewer.getName());

    return newTeachers;
  }

  public List<Integer> removeTeachers(Integer reviewerId, List<Integer> teacherIds) {
    var reviewer = findById(reviewerId);

    List<Teacher> deletedTeachers = reviewer.getTeachers().stream()
        .filter(t -> contains(teacherIds, t.getId()))
        .toList();

    reviewer.getTeachers().removeAll(deletedTeachers);
    reviewerRepository.save(reviewer);

    generatorHostService.removeTeacher(reviewerId, deletedTeachers);

    log.info("Teachers [{}] are deleted from reviewer {}",
        deletedTeachers.stream()
            .map(t -> t.getFirstName() + " " + t.getLastName())
            .collect(Collectors.joining(", ")),
        reviewer.getName());
    return teacherIds;
  }

  private Reviewer findById(Integer reviewerId) {
    return reviewerRepository.findById(reviewerId)
        .orElseThrow(() -> new RuntimeException("Reviewer not found"));
  }
}
