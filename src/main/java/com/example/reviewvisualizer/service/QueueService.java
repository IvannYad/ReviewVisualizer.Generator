package com.example.reviewvisualizer.service;

import com.example.reviewvisualizer.dto.CreateReviewDto;
import com.example.reviewvisualizer.model.Review;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueueService {

  private final ObjectMapper objectMapper;
  private final JdbcTemplate sqliteJdbcTemplate;

  @Autowired
  public QueueService(ObjectMapper objectMapper, JdbcTemplate sqliteJdbcTemplate) {
    this.objectMapper = objectMapper;
    this.sqliteJdbcTemplate = sqliteJdbcTemplate;
  }

  @Transactional
  public void addReview(CreateReviewDto review) {
    String sql = "INSERT INTO Queue (Data) VALUES (?)";
    try {
      String reviewJson = objectMapper.writeValueAsString(review);
      sqliteJdbcTemplate.update(sql, reviewJson);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Something went wrong while serializing the review", e);
    }
  }

  @Transactional
  public Review getReview() {
    String selectSql = "SELECT result.Data FROM (SELECT queue.Data, min(queue.Id) FROM Queue queue) result";
    String deleteSql = "DELETE FROM Queue WHERE Id = (SELECT min(queue2.Id) FROM Queue queue2)";

    try {
      String data = sqliteJdbcTemplate.queryForObject(selectSql, String.class);
      Review review = objectMapper.readValue(data, Review.class);

      sqliteJdbcTemplate.update(deleteSql);
      return review;
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Something went wrong while deserializing the review", e);
    }
  }

  public int getQueueSize() {
    String sql = "SELECT COUNT(*) FROM Queue";
    try {
      return sqliteJdbcTemplate.queryForObject(sql, Integer.class);
    } catch (RuntimeException e) {
      throw new RuntimeException("Failed to fetch queue size", e);
    }
  }
}
