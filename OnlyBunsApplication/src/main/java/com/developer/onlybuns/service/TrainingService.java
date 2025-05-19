package com.developer.onlybuns.service;

import com.developer.onlybuns.dto.request.TrainingDTO;
import com.developer.onlybuns.entity.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    Optional<Training> findById(Integer id);

    List<Training> findAll();

    Training saveTraining(Training training);

    List<TrainingDTO> getTrainingsForDay(LocalDate date);

    List<TrainingDTO> getTrainingsForWeek(LocalDate startOfWeek);

    void bookTraining(Integer trainingId, String username);

    List<TrainingDTO> getBookedTrainingsForUser(String username);

    void cancelBooking(Integer trainingId, String username);
}
