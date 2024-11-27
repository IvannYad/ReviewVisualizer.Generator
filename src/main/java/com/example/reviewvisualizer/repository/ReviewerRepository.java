package com.example.reviewvisualizer.repository;

import com.example.reviewvisualizer.model.Reviewer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewerRepository extends JpaRepository<Reviewer, Integer> {

    @Query("SELECT reviewer FROM Reviewer reviewer JOIN FETCH reviewer.teachers")
    public List<Reviewer> findAllWithTeachers();
}
