package com.example.reviewvisualizer.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import com.example.reviewvisualizer.model.Reviewer;
import com.example.reviewvisualizer.model.Teacher;
import com.example.reviewvisualizer.repository.ReviewerRepository;
import jakarta.annotation.PostConstruct;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GeneratorHostService {
  private static final Logger logger = LoggerFactory.getLogger(GeneratorHostService.class);

  private final QueueService queueService;
  private final List<Reviewer> reviewers;
  private final ConcurrentHashMap<Reviewer, Thread> reviewersCollection;
  private boolean isInitialized = false;

  @Autowired
  public GeneratorHostService(QueueService queueService, ReviewerRepository reviewerRepository) {
    this.queueService = queueService;
    this.reviewers = reviewerRepository.findAllWithTeachers();
    this.reviewersCollection = new ConcurrentHashMap<>();
  }

  @PostConstruct
  public void init() {
    if (isInitialized)
      return;

    reviewers.forEach(reviewer ->
        reviewersCollection.put(reviewer,
            new Thread(() -> reviewer.generateReview(queueService, logger)))
    );
    reviewers.forEach(reviewer -> reviewer.setThreadCompletedListener(this::onWorkerStopped));

    isInitialized = true;
  }

  public void start() {
    if (!isInitialized)
      return;

    logger.info("[GeneratorHost] Generator Host started");

    reviewersCollection.forEach((reviewer, thread) -> {
      if (!reviewer.isStopped()) {
        logger.info("[GeneratorHost] Reviewer {} is started", reviewer.getName());
        thread.start();
      } else {
        reviewersCollection.put(reviewer, new EmptyThread());
      }
    });
  }

  public boolean createReviewer(Reviewer reviewer) {
    if (!isInitialized || reviewersCollection.containsKey(reviewer)) {
      return false;
    }

    reviewer.setStopped(true);
    reviewer.setThreadCompletedListener(this::onWorkerStopped);
    reviewersCollection.put(reviewer, new EmptyThread());
    logger.info("[GeneratorHost] Reviewer {} is created in stopped state", reviewer.getName());
    return true;
  }

  public boolean deleteReviewer(Reviewer reviewer) {
    if (!isInitialized || !reviewersCollection.containsKey(reviewer))
      return true;

    try {
      Thread thread = reviewersCollection.get(reviewer);
      if (thread != null) {
        thread.interrupt();
        thread.join();
      }
      reviewersCollection.put(reviewer, new EmptyThread());
      reviewersCollection.remove(reviewer);
      logger.info("[GeneratorHost] Reviewer {} is deleted", reviewer.getName());
    } catch (InterruptedException e) {
      logger.error("[GeneratorHost] Error stopping reviewer {}", reviewer.getName(), e);
      return false;
    }

    return true;
  }

  public boolean stopReviewer(int id) {
    if (!isInitialized)
      return false;

    Reviewer reviewer = reviewersCollection.keySet().stream()
        .filter(r -> r.getId().equals(id))
        .findFirst()
        .orElse(null);

    if (reviewer == null) {
      return false;
    }

    logger.info("[GeneratorHost] Stopping reviewer {}", reviewer.getName());
    reviewer.setStopped(true);
    return true;
  }

  public boolean addTeacher(int reviewerId, List<Teacher> newTeachers) {
    if (!isInitialized)
      return false;

    Reviewer reviewer = reviewersCollection.keySet().stream()
            .filter(r -> r.getId().equals(reviewerId))
            .findFirst()
            .orElse(null);

    if (reviewer == null) {
      return false;
    }

    logger.info("[GeneratorHost] Adding teacher reviewer {}", reviewer.getName());
    reviewer.getTeachers().addAll(newTeachers);
    return true;
  }

  public boolean removeTeacher(int reviewerId, List<Teacher> newTeachers) {
    if (!isInitialized)
      return false;

    Reviewer reviewer = reviewersCollection.keySet().stream()
            .filter(r -> r.getId().equals(reviewerId))
            .findFirst()
            .orElse(null);

    if (reviewer == null) {
      return false;
    }

    logger.info("[GeneratorHost] Removing teacher reviewer {}", reviewer.getName());
    reviewer.getTeachers().addAll(newTeachers);
    return true;
  }

  public boolean startReviewer(int id) {
    if (!isInitialized)
      return false;

    Reviewer reviewer = reviewersCollection.keySet().stream()
        .filter(r -> r.getId().equals(id))
        .findFirst()
        .orElse(null);

    if (reviewer == null || !reviewer.isStopped() || reviewer.getTeachers() == null ||
        reviewer.getTeachers().isEmpty()) {
      return false;
    }

    logger.info("[GeneratorHost] Starting reviewer {}", reviewer.getName());
    reviewer.setStopped(false);
    reviewersCollection.put(reviewer, new Thread(() -> reviewer.generateReview(queueService, logger)));
    reviewersCollection.get(reviewer).start();
    return true;
  }

  private void onWorkerStopped(Reviewer reviewer) {
    if (!isInitialized)
      return;

    logger.info("[GeneratorHost] Reviewer {} is stopped", reviewer.getName());
    reviewersCollection.put(reviewer, new EmptyThread());
  }
}
