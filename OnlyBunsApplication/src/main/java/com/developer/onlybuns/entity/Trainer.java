package com.developer.onlybuns.entity;


import com.developer.onlybuns.enums.Role;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="trainer")
public class Trainer extends User {

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    public Trainer() {
    }


    public Trainer(List<Training> trainings) {
        this.trainings = trainings;
    }

    public Trainer(Integer id, String username, String email, String password, String name, String surname, String phoneNumber, Role role, String activationToken, boolean isVerified, List<Training> trainings) {
        super(id, username, email, password, name, surname, phoneNumber, role, activationToken, isVerified);
        this.trainings = trainings;
    }

    public Trainer(String username, String email, String password, String name, String surname, String phoneNumber, Role role, String activationToken, boolean isVerified, List<Training> trainings) {
        super(username, email, password, name, surname, phoneNumber, role, activationToken, isVerified);
        this.trainings = trainings;
    }

    public Trainer(String username, String email, String password, String name, String surname, String phoneNumber, Role role, boolean isVerified, List<Training> trainings) {
        super(username, email, password, name, surname, phoneNumber, role, isVerified);
        this.trainings = trainings;
    }

    public Trainer(Integer id, String username, String email, String password, String name, String surname, String phoneNumber, Role role, boolean isVerified, List<Training> trainings) {
        super(id, username, email, password, name, surname, phoneNumber, role, isVerified);
        this.trainings = trainings;
    }

    public List<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(List<Training> trainings) {
        this.trainings = trainings;
    }
}
