package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.dto.request.RegisteredUserDTO;
import com.developer.onlybuns.dto.request.TrainingDTO;
import com.developer.onlybuns.entity.RegisteredUser;
import com.developer.onlybuns.entity.Trainer;
import com.developer.onlybuns.entity.Training;
import com.developer.onlybuns.repository.RegisteredUserRepository;
import com.developer.onlybuns.repository.TrainerRepository;
import com.developer.onlybuns.repository.TrainingRepository;
import com.developer.onlybuns.service.RegisteredUserService;
import com.developer.onlybuns.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class TrainingServiceImpl implements TrainingService {

    @Autowired
    private final TrainingRepository trainingRepository;

    @Autowired
    private final RegisteredUserRepository registeredUserRepository;

    public TrainingServiceImpl(TrainingRepository trainingRepository, RegisteredUserRepository registeredUserRepository) {
        this.trainingRepository = trainingRepository;
        this.registeredUserRepository = registeredUserRepository;
    }

    public Optional<Training> findById(Integer id) {
        return trainingRepository.findById(id);
    }

    public List<Training> findAll() {
        return trainingRepository.findAll();
    }

    public Training saveTraining(Training training) {
        return trainingRepository.save(training);
    }


    public List<TrainingDTO> getTrainingsForDay(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Training> trainings = trainingRepository.findByStartTimeBetween(startOfDay, endOfDay);
        List<TrainingDTO> trainingsDTO = new ArrayList<TrainingDTO>();

        for (Training training : trainings) {
            Set<RegisteredUserDTO> usersDTO = new HashSet<RegisteredUserDTO>();
            for (RegisteredUser user : training.getUsers()) {
                RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO(user.getUsername(), user.getEmail(), user.getName(), user.getSurname(), user.getPhoneNumber());
                usersDTO.add(registeredUserDTO);
            }
            TrainingDTO trainingDTO = new TrainingDTO(training.getId(), training.getDuration(), training.getStartTime(), training.getTrainingType(), training.getTrainer().getUsername(), usersDTO);
            trainingsDTO.add(trainingDTO);
        }

        return trainingsDTO;
    }

    public List<TrainingDTO> getTrainingsForWeek(LocalDate startOfWeek) {
        LocalDateTime startDateTime = startOfWeek.atStartOfDay();
        LocalDateTime endDateTime = startOfWeek.plusDays(6).atTime(LocalTime.MAX);
        List<Training> trainings = trainingRepository.findByStartTimeBetween(startDateTime, endDateTime);
        List<TrainingDTO> trainingsDTO = new ArrayList<TrainingDTO>();

        for (Training training : trainings) {
            Set<RegisteredUserDTO> usersDTO = new HashSet<RegisteredUserDTO>();
            for (RegisteredUser user : training.getUsers()) {
                RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO(user.getUsername(), user.getEmail(), user.getName(), user.getSurname(), user.getPhoneNumber());
                usersDTO.add(registeredUserDTO);
            }
            TrainingDTO trainingDTO = new TrainingDTO(training.getId(), training.getDuration(), training.getStartTime(), training.getTrainingType(), training.getTrainer().getUsername(), usersDTO);
            trainingsDTO.add(trainingDTO);
        }

        return trainingsDTO;
    }

    @Transactional
    public void bookTraining(Integer trainingId, String username) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found"));

        RegisteredUser user = registeredUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (training.getUsers().contains(user)) {
            throw new RuntimeException("You already booked this training");
        }

        training.getUsers().add(user);
        user.getTrainings().add(training);
        trainingRepository.save(training);
        registeredUserRepository.save(user);
    }
}
