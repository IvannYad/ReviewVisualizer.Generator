package com.example.reviewvisualizer.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewerDto {
  @NotBlank
  private String name;

  @NotNull
  @Min(100)
  @Max(100000)
  private Integer reviewGenerationFrequencyMiliseconds;

  @NotNull
  @Min(1)
  @Max(100)
  private Integer teachingQualityMinGrade;

  @NotNull
  @Min(1)
  @Max(100)
  private Integer teachingQualityMaxGrade;

  @NotNull
  @Min(1)
  @Max(100)
  private Integer studentsSupportMinGrade;

  @NotNull
  @Min(1)
  @Max(100)
  private Integer studentsSupportMaxGrade;

  @NotNull
  @Min(1)
  @Max(100)
  private Integer communicationMinGrade;

  @NotNull
  @Min(1)
  @Max(100)
  private Integer communicationMaxGrade;

  private boolean isStopped = true;
}
