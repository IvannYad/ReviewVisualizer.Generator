package com.example.reviewvisualizer.controller;

import java.util.List;

import com.example.reviewvisualizer.dto.CreateReviewerDto;
import com.example.reviewvisualizer.model.Reviewer;
import com.example.reviewvisualizer.model.Teacher;
import com.example.reviewvisualizer.service.ReviewerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/reviewers")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewerController {
  public final ReviewerService reviewerService;

  @GetMapping
  public ResponseEntity<List<Reviewer>> getReviewers() {
    return ResponseEntity.ok(reviewerService.findAllWithTeachers());
  }

  @PostMapping
  public ResponseEntity<Boolean> createReviewer(@RequestBody @Valid CreateReviewerDto reviewer) {
    return ResponseEntity.ok(reviewerService.createReviewer(reviewer));
  }

  @DeleteMapping
  public ResponseEntity<HttpStatus> deleteReviewer(@RequestParam("reviewerId") Integer reviewerId) {
    return ResponseEntity.ok(reviewerService.deleteReviewer(reviewerId));
  }

  @PostMapping("/stop-reviewer/{id}")
  public ResponseEntity<Boolean> stopReviewer(@PathVariable("id") Integer id) {
    return ResponseEntity.ok(reviewerService.stopReviewerById(id));
  }

  @PostMapping("/start-reviewer/{id}")
  public ResponseEntity<Boolean> startReviewer(@PathVariable("id") Integer id) {
    return ResponseEntity.ok(reviewerService.startReviewerById(id));
  }

  @PostMapping("/add-teachers")
  public ResponseEntity<List<Teacher>> addTeachers(
      @RequestParam("reviewerId") Integer reviewerId,
      @RequestBody List<Integer> teacherIds) {
    return ResponseEntity.ok(reviewerService.addTeachers(reviewerId, teacherIds));
  }

  @PostMapping("/remove-teachers")
  public ResponseEntity<List<Integer>> removeTeachers(
      @RequestParam("reviewerId") Integer reviewerId,
      @RequestBody List<Integer> teacherIds) {
    return ResponseEntity.ok(reviewerService.removeTeachers(reviewerId, teacherIds));
  }
}
