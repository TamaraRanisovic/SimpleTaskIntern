package com.developer.onlybuns.entity;


import com.developer.onlybuns.enums.TrainingType;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="training")
public class Training {
    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    private Integer id;


    @Column(name="duration", unique=false, nullable=false)
    private Integer duration;


    @Column(name="cancelDeadline", unique=false, nullable=false)
    private Integer cancelDeadline;


    @Column(name="startTime", unique=false, nullable=false)
    private LocalDateTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(name="type", unique=false, nullable=false)
    private TrainingType trainingType;


    @ManyToMany(mappedBy = "trainings")
    private Set<RegisteredUser> users;

    @ManyToOne
    @JoinColumn(name = "trainer_id", nullable = false)
    private Trainer trainer;

    public Training() {
        this.users = new HashSet<RegisteredUser>();
    }

    public Training(Integer id, Integer duration, Integer cancelDeadline, LocalDateTime startTime, TrainingType trainingType, Set<RegisteredUser> users, Trainer trainer) {
        this.id = id;
        this.duration = duration;
        this.cancelDeadline = cancelDeadline;
        this.startTime = startTime;
        this.trainingType = trainingType;
        this.users = users;
        this.trainer = trainer;
    }

    public Training(Integer duration, Integer cancelDeadline, LocalDateTime startTime, TrainingType trainingType, Set<RegisteredUser> users, Trainer trainer) {
        this.duration = duration;
        this.cancelDeadline = cancelDeadline;
        this.startTime = startTime;
        this.trainingType = trainingType;
        this.users = users;
        this.trainer = trainer;
    }

    public Training(Integer duration, Integer cancelDeadline, LocalDateTime startTime, TrainingType trainingType, Trainer trainer) {
        this.duration = duration;
        this.cancelDeadline = cancelDeadline;
        this.startTime = startTime;
        this.trainingType = trainingType;
        this.trainer = trainer;
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

    public Set<RegisteredUser> getUsers() {
        return users;
    }

    public void setUsers(Set<RegisteredUser> users) {
        this.users = users;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public TrainingType getTrainingType() {
        return trainingType;
    }

    public void setTrainingType(TrainingType trainingType) {
        this.trainingType = trainingType;
    }

    public Integer getCancelDeadline() {
        return cancelDeadline;
    }

    public void setCancelDeadline(Integer cancelDeadline) {
        this.cancelDeadline = cancelDeadline;
    }
}
