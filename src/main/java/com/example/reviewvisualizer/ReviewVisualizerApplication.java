package com.example.reviewvisualizer;

import com.example.reviewvisualizer.service.GeneratorHostService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@RequiredArgsConstructor
public class ReviewVisualizerApplication {
  private final GeneratorHostService generatorHostService;

  public static void main(String[] args) {
    SpringApplication.run(ReviewVisualizerApplication.class, args);
  }

  @Bean
  public CommandLineRunner run() {
    return args -> {
      generatorHostService.init();
      generatorHostService.start();
    };
  }
}
