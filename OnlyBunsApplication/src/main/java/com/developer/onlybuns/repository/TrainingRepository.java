package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Integer> {
    List<Training> findByStartTimeBetween(LocalDateTime start, LocalDateTime end);
    List<Training> findByTrainerUsername(String username);

}
