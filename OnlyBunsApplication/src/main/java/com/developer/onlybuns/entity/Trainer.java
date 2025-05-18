package com.developer.onlybuns.entity;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="trainer")
public class Trainer extends User {

    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Training> trainings;

    public Trainer() {
    }



}
