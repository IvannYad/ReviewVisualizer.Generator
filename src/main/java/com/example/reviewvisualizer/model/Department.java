package com.example.reviewvisualizer.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "Departments")
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private int id;

  @NotBlank
  @Column(name = "Name")
  private String name;

  @NotBlank
  @Column(name = "LogoUrl")
  private String logoUrl;

  @Min(1)
  @Max(100)
  @ColumnDefault("0.0")
  @Column(name = "Rating")
  private Double rating;
}