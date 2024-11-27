package com.example.reviewvisualizer.mapper;

import com.example.reviewvisualizer.dto.CreateReviewerDto;
import com.example.reviewvisualizer.model.Reviewer;
import org.mapstruct.Mapper;

@Mapper
public interface ReviewerMapper {
  Reviewer toModel(CreateReviewerDto createReviewerDto);
}
