package com.developer.onlybuns.dto.request;


import com.developer.onlybuns.enums.TrainingType;

import java.time.LocalDateTime;
import java.util.Set;

public class NewTrainingDTO {

    private Integer duration;

    private LocalDateTime startTime;

    private TrainingType trainingType;

    private String trainer;

    private Integer cancelDeadline;

    public NewTrainingDTO() {
    }

    public NewTrainingDTO(Integer duration, LocalDateTime startTime, TrainingType trainingType, String trainer, Integer cancelDeadline) {
        this.duration = duration;
        this.startTime = startTime;
        this.trainingType = trainingType;
        this.trainer = trainer;
        this.cancelDeadline = cancelDeadline;
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

    public Integer getCancelDeadline() {
        return cancelDeadline;
    }

    public void setCancelDeadline(Integer cancelDeadline) {
        this.cancelDeadline = cancelDeadline;
    }
}