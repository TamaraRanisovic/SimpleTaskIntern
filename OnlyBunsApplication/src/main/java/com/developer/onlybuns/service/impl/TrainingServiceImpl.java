package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.dto.request.NewTrainingDTO;
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
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
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

    @Autowired
    private final TrainerRepository trainerRepository;

    @Autowired
    private JavaMailSender mailSender;

    public TrainingServiceImpl(TrainingRepository trainingRepository, RegisteredUserRepository registeredUserRepository, TrainerRepository trainerRepository) {
        this.trainingRepository = trainingRepository;
        this.registeredUserRepository = registeredUserRepository;
        this.trainerRepository = trainerRepository;
    }

    public TrainingDTO findById(Integer id) {
        Training training = trainingRepository.getById(id);

        if (training == null) {
            throw new EntityNotFoundException("Training with id " + id + " not found.");
        }

        Set<RegisteredUserDTO> usersDTO = new HashSet<RegisteredUserDTO>();
        for (RegisteredUser user : training.getUsers()) {
            RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO(user.getUsername(), user.getEmail(), user.getName(), user.getSurname(), user.getPhoneNumber());
            usersDTO.add(registeredUserDTO);
        }
        TrainingDTO trainingDTO = new TrainingDTO(training.getId(), training.getDuration(), training.getStartTime(), training.getTrainingType(), training.getCancelDeadline(), training.getTrainer().getUsername(), usersDTO);

        return trainingDTO;
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
            TrainingDTO trainingDTO = new TrainingDTO(training.getId(), training.getDuration(), training.getStartTime(), training.getTrainingType(), training.getCancelDeadline(), training.getTrainer().getUsername(), usersDTO);
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
            TrainingDTO trainingDTO = new TrainingDTO(training.getId(), training.getDuration(), training.getStartTime(), training.getTrainingType(), training.getCancelDeadline(), training.getTrainer().getUsername(), usersDTO);
            trainingsDTO.add(trainingDTO);
        }

        return trainingsDTO;
    }

    @Override
    public List<TrainingDTO> getTrainingsForDayByTrainer(LocalDate date, String trainerUsername) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);

        Trainer trainer = trainerRepository.findByUsername(trainerUsername);
        if (trainer == null) {
            throw new EntityNotFoundException("Trainer with username " + trainerUsername + " not found.");
        }

        List<TrainingDTO> trainingsByTrainer = new ArrayList<TrainingDTO>();
        List<TrainingDTO> trainingsDTO = getTrainingsForDay(date);
        for (TrainingDTO trainingDTO : trainingsDTO) {
            if (trainingDTO.getTrainer().equals(trainerUsername)) {
                trainingsByTrainer.add(trainingDTO);
            }
        }
        return trainingsByTrainer;
    }

    @Override
    public List<TrainingDTO> getTrainingsForWeekByTrainer(LocalDate startOfWeek, String trainerUsername) {
        LocalDateTime start = startOfWeek.atStartOfDay();
        LocalDateTime end = start.plusDays(6).with(LocalTime.MAX);

        Trainer trainer = trainerRepository.findByUsername(trainerUsername);
        if (trainer == null) {
            throw new EntityNotFoundException("Trainer with username " + trainerUsername + " not found.");
        }
        List<TrainingDTO> trainingsByTrainer = new ArrayList<TrainingDTO>();
        List<TrainingDTO> trainingsDTO = getTrainingsForWeek(startOfWeek);
        for (TrainingDTO trainingDTO : trainingsDTO) {
            if (trainingDTO.getTrainer().equals(trainerUsername)) {
                trainingsByTrainer.add(trainingDTO);
            }
        }
        return trainingsByTrainer;
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

        try {
            TrainingDTO trainingDTO = findById(trainingId);
            sendBookingEmail(user.getEmail(), trainingDTO);
        } catch (Exception e) {
            System.err.println("Failed to send booking email: " + e.getMessage());
        }

    }


    @Transactional
    public void cancelBooking(Integer trainingId, String username) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new RuntimeException("Training not found"));

        RegisteredUser user = registeredUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!training.getUsers().contains(user)) {
            throw new RuntimeException("User has not booked this training");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = training.getStartTime(); // Assuming this is LocalDateTime
        int cancelDeadlineHours = training.getCancelDeadline(); // Number of hours before start

        LocalDateTime latestCancelTime = startTime.minusHours(cancelDeadlineHours);

        if (now.isAfter(latestCancelTime)) {
            throw new IllegalStateException(
                    String.format("You can no longer cancel this booking. Cancellations must be made at least %d hours before training start.", cancelDeadlineHours)
            );
        }

        training.getUsers().remove(user);
        user.getTrainings().remove(training);
        trainingRepository.save(training);
        registeredUserRepository.save(user);

        try {
            TrainingDTO trainingDTO = findById(training.getId());
            sendTrainingCancellationEmail(user.getEmail(), trainingDTO);
        } catch (Exception e) {
            System.err.println("Failed to send training cancellation email: " + e.getMessage());
        }
    }

    public List<TrainingDTO> getBookedTrainingsForUser(String username) {
        RegisteredUser user = registeredUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));


        Set<Training> trainings = user.getTrainings();
        List<TrainingDTO> trainingsDTO = new ArrayList<TrainingDTO>();

        for (Training training : trainings) {
            Set<RegisteredUserDTO> usersDTO = new HashSet<RegisteredUserDTO>();
            for (RegisteredUser user1 : training.getUsers()) {
                RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO(user1.getUsername(), user1.getEmail(), user1.getName(), user1.getSurname(), user1.getPhoneNumber());
                usersDTO.add(registeredUserDTO);
            }
            TrainingDTO trainingDTO = new TrainingDTO(training.getId(), training.getDuration(), training.getStartTime(), training.getTrainingType(), training.getCancelDeadline(), training.getTrainer().getUsername(), usersDTO);
            trainingsDTO.add(trainingDTO);
        }

        return trainingsDTO;
    }


    @Override
    public void createTraining(NewTrainingDTO dto) {
        // Validate the incoming DTO
        validateTrainingInput(dto);

        // Attempt to find the trainer by username
        Trainer trainer = trainerRepository.findByUsername(dto.getTrainer());
        if (trainer == null) {
            throw new EntityNotFoundException("Trainer with username '" + dto.getTrainer() + "' not found.");
        }

        // Create the training entity
        Training training = new Training();
        training.setDuration(dto.getDuration());
        training.setStartTime(dto.getStartTime());
        training.setTrainingType(dto.getTrainingType());
        training.setTrainer(trainer);
        training.setCancelDeadline(dto.getCancelDeadline());

        // Save the training
        trainingRepository.save(training);

        try {
            TrainingDTO trainingDTO = findById(training.getId());
            sendBookingEmail(trainer.getEmail(), trainingDTO);
        } catch (Exception e) {
            System.err.println("Failed to send booking email: " + e.getMessage());
        }
    }


    private void validateTrainingInput(NewTrainingDTO dto) {
        int minutes = dto.getStartTime().getMinute();
        int duration = dto.getDuration();

        if (!(duration == 30 || duration == 60)) {
            throw new IllegalArgumentException("Duration must be 30 or 60 minutes.");
        }

        if (!(minutes == 0 || minutes == 30)) {
            throw new IllegalArgumentException("Start time must be on the hour or half-hour.");
        }
    }

    @Override
    public List<TrainingDTO> getTrainingsByTrainerUsername(String username) {
        Trainer trainer = trainerRepository.findByUsername(username);
        if (trainer == null) {
            throw new EntityNotFoundException("Trainer with username '" + username + "' not found.");
        }


        List<Training> trainings = trainingRepository.findByTrainerUsername(username);
        List<TrainingDTO> trainingsDTO = new ArrayList<TrainingDTO>();

        for (Training training : trainings) {
            Set<RegisteredUserDTO> usersDTO = new HashSet<RegisteredUserDTO>();
            for (RegisteredUser user1 : training.getUsers()) {
                RegisteredUserDTO registeredUserDTO = new RegisteredUserDTO(user1.getUsername(), user1.getEmail(), user1.getName(), user1.getSurname(), user1.getPhoneNumber());
                usersDTO.add(registeredUserDTO);
            }
            TrainingDTO trainingDTO = new TrainingDTO(training.getId(), training.getDuration(), training.getStartTime(), training.getTrainingType(), training.getCancelDeadline(), training.getTrainer().getUsername(), usersDTO);
            trainingsDTO.add(trainingDTO);
        }

        return trainingsDTO;
    }

    @Override
    public void deleteTrainingById(Integer id) {
        Training training = trainingRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Training with id " + id + " not found."));

        // Manually remove training from each user's set of trainings
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = training.getStartTime(); // Assuming this is LocalDateTime
        int cancelDeadlineHours = training.getCancelDeadline(); // Number of hours before start

        LocalDateTime latestCancelTime = startTime.minusHours(cancelDeadlineHours);

        if (now.isAfter(latestCancelTime)) {
            throw new IllegalStateException(
                    String.format("You can no longer cancel this booking. Cancellations must be made at least %d hours before training start.", cancelDeadlineHours)
            );
        }
        TrainingDTO trainingDTO = findById(training.getId());

        Set<RegisteredUser> users = training.getUsers();
        if (users != null) {
            for (RegisteredUser user : users) {
                user.getTrainings().remove(training);
            }
            // Clear the relationship from the training side as well
            training.getUsers().clear();
        }

        trainingRepository.delete(training);

        try {
            Trainer trainer = trainerRepository.findByUsername(trainingDTO.getTrainer());
            sendTrainingCancellationEmail(trainer.getEmail(), trainingDTO);
        } catch (Exception e) {
            System.err.println("Failed to send training cancellation email: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public void cancelBookingByUser(Integer trainingId, String username) {
        Training training = trainingRepository.findById(trainingId)
                .orElseThrow(() -> new EntityNotFoundException("Training not found"));

        RegisteredUser user = registeredUserRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        // Check if user is actually booked for this training
        if (!training.getUsers().contains(user)) {
            throw new EntityNotFoundException("User is not booked for this training");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = training.getStartTime(); // Assuming this is LocalDateTime
        int cancelDeadlineHours = training.getCancelDeadline(); // Number of hours before start

        LocalDateTime latestCancelTime = startTime.minusHours(cancelDeadlineHours);

        if (now.isAfter(latestCancelTime)) {
            throw new IllegalStateException(
                    String.format("You can no longer cancel this booking. Cancellations must be made at least %d hours before training start.", cancelDeadlineHours)
            );
        }

        // Remove the user from both sides of the relationship
        training.getUsers().remove(user);
        user.getTrainings().remove(training);

        trainingRepository.save(training);
        registeredUserRepository.save(user);

        try {
            TrainingDTO trainingDTO = findById(training.getId());
            sendTrainingCancellationEmail(user.getEmail(), trainingDTO);
        } catch (Exception e) {
            System.err.println("Failed to send training cancellation email: " + e.getMessage());
        }
    }


    private void sendBookingEmail(String email, TrainingDTO trainingDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ranisovic.in1.2020@uns.ac.rs");
        message.setTo(email);
        message.setSubject("Training Booking Confirmation");

        StringBuilder emailBody = new StringBuilder();
        emailBody.append("You have successfully booked the following training:\n\n");
        emailBody.append("🏋️ Training Type: ").append(trainingDTO.getTrainingType()).append("\n");
        emailBody.append("🕒 Start Time: ").append(trainingDTO.getStartTime()).append("\n");
        emailBody.append("⏱️ Duration: ").append(trainingDTO.getDuration()).append(" minutes\n");
        emailBody.append("👤 Trainer: ").append(trainingDTO.getTrainer()).append("\n");
        emailBody.append("❗ Cancel Deadline: ").append(trainingDTO.getCancelDeadline())
                .append(" hours before the start time\n\n");
        emailBody.append("Thank you for booking! We look forward to seeing you.\n");
        emailBody.append("Best regards,\nYour Training Center Team");

        message.setText(emailBody.toString());
        mailSender.send(message);
    }


    private void sendTrainingCancellationEmail(String email, TrainingDTO trainingDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ranisovic.in1.2020@uns.ac.rs");
        message.setTo(email);
        message.setSubject("Training Booking Cancelled");

        StringBuilder emailBody = new StringBuilder();
        emailBody.append("You have successfully cancelled your booking for the following training:\n\n");
        emailBody.append("🏋️ Training Type: ").append(trainingDTO.getTrainingType()).append("\n");
        emailBody.append("🕒 Start Time: ").append(trainingDTO.getStartTime()).append("\n");
        emailBody.append("⏱️ Duration: ").append(trainingDTO.getDuration()).append(" minutes\n");
        emailBody.append("👤 Trainer: ").append(trainingDTO.getTrainer()).append("\n");
        emailBody.append("❗ Cancellation Deadline: ").append(trainingDTO.getCancelDeadline())
                .append(" hours before start time\n\n");
        emailBody.append("We're sorry to see you go! If this was a mistake, feel free to book again.\n\n");
        emailBody.append("Best regards,\nYour Training Center Team");

        message.setText(emailBody.toString());
        mailSender.send(message);
    }


}
