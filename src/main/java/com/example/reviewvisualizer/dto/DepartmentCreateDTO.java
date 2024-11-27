package com.example.reviewvisualizer.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentCreateDTO {
  @NotBlank
  @Pattern(regexp = "^[A-Z]+$")
  private String name;

  @NotBlank
  private String logoUrl;

  @Min(value = 1)
  @Max(value = 100)
  private Double rating;
}
