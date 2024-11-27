package com.example.reviewvisualizer.model;

import java.util.List;

import com.example.reviewvisualizer.model.enums.AcademicDegree;
import com.example.reviewvisualizer.model.enums.AcademicRank;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

@Data
@Entity
@Table(name = "Teachers")
public class Teacher {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "Id")
  private Integer id;

  @NotBlank
  @Column(name = "FirstName")
  private String firstName;

  @NotBlank
  @Column(name = "LastName")
  private String lastName;

  @NotNull
  @Column(name = "AcademicDegree")
  private AcademicDegree academicDegree;

  @NotNull
  @Column(name = "AcademicRank")
  private AcademicRank academicRank;

  @NotBlank
  @Column(name = "PhotoUrl")
  private String photoUrl;

  @Min(1)
  @Max(100)
  @ColumnDefault("0.0")
  @Column(name = "Rating")
  private Double rating;

  @NotNull
  @Column(name = "DepartmentId")
  private Integer departmentId;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "DepartmentId", insertable = false, updatable = false)
  private Department department;

  @Transient
  @ManyToMany(mappedBy = "teachers", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Reviewer> reviewers;
}
