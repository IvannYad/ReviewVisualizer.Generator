package com.example.reviewvisualizer.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewDto {
  @FutureOrPresent(message = "Review time must be in the future or present.")
  @JsonProperty("ReviewTime")
  private LocalDateTime reviewTime;

  @Min(1)
  @Max(100)
  @NotNull
  @JsonProperty("TeachingQuality")
  private int teachingQuality;

  @Min(1)
  @Max(100)
  @NotNull
  @JsonProperty("StudentsSupport")
  private int studentsSupport;

  @Min(1)
  @Max(100)
  @NotNull
  @JsonProperty("Communication")
  private int communication;

  @Min(1)
  @Max(100)
  @NotNull
  @JsonProperty("Overall")
  private double overall;

  @NotNull
  @JsonProperty("TeacherId")
  private int teacherId;
}
