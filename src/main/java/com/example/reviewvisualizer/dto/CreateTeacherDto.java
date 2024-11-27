package com.example.reviewvisualizer.dto;

import com.example.reviewvisualizer.model.enums.AcademicDegree;
import com.example.reviewvisualizer.model.enums.AcademicRank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeacherDto {
  @NotBlank
  private String firstName;

  @NotBlank
  private String lastName;

  @NotNull
  private AcademicDegree academicDegree;

  @NotNull
  private AcademicRank academicRank;

  @NotBlank
  private String photoUrl;

  @Min(value = 1)
  @Max(value = 100)
  private Double rating;

  @NotNull
  private Integer departmentId;
}
