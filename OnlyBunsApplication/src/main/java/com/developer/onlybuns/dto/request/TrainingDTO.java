package com.developer.onlybuns.dto.request;

import com.developer.onlybuns.entity.RegisteredUser;
import com.developer.onlybuns.entity.Trainer;
import com.developer.onlybuns.enums.TrainingType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class TrainingDTO {

    private Integer id;

    private Integer duration;

    private LocalDateTime startTime;

    private TrainingType trainingType;

    private String trainer;

    private Set<RegisteredUserDTO> users;

    public TrainingDTO() {
        this.users = new HashSet<RegisteredUserDTO>();
    }

    public TrainingDTO(Integer id, Integer duration, LocalDateTime startTime, TrainingType trainingType, String trainer, Set<RegisteredUserDTO> users) {
        this.id = id;
        this.duration = duration;
        this.startTime = startTime;
        this.trainingType = trainingType;
        this.trainer = trainer;
        this.users = users;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    public Set<RegisteredUserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<RegisteredUserDTO> users) {
        this.users = users;
    }
}
