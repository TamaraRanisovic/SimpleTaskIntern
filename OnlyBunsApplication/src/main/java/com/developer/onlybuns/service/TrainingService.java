package com.developer.onlybuns.service;

import com.developer.onlybuns.dto.request.NewTrainingDTO;
import com.developer.onlybuns.dto.request.TrainingDTO;
import com.developer.onlybuns.entity.Training;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainingService {
    TrainingDTO findById(Integer id);

    List<Training> findAll();

    Training saveTraining(Training training);

    List<TrainingDTO> getTrainingsForDay(LocalDate date);

    List<TrainingDTO> getTrainingsForWeek(LocalDate startOfWeek);

    void bookTraining(Integer trainingId, String username);

    List<TrainingDTO> getBookedTrainingsForUser(String username);

    void cancelBooking(Integer trainingId, String username);

    void createTraining(NewTrainingDTO dto);

    List<TrainingDTO> getTrainingsByTrainerUsername(String username);

    void deleteTrainingById(Integer id);

    List<TrainingDTO> getTrainingsForDayByTrainer(LocalDate date, String trainerUsername);
    List<TrainingDTO> getTrainingsForWeekByTrainer(LocalDate startOfWeek, String trainerUsername);

    void cancelBookingByUser(Integer trainingId, String username);

}
