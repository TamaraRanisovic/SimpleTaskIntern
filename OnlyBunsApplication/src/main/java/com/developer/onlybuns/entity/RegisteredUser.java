package com.developer.onlybuns.entity;


import com.developer.onlybuns.enums.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="registeredUser")
public class RegisteredUser extends User {


    @ManyToMany
    @JoinTable(
            name = "booked_trainings",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id")
    )
    private Set<Training> trainings = new HashSet<>();



    public RegisteredUser() {
        this.trainings = trainings;
    }


    public RegisteredUser(Integer id, String korisnickoIme, String email, String password, String ime, String prezime, String broj, Role role, String activationToken, boolean verifikacija, Set<Training> trainings) {
        super(id, korisnickoIme, email, password, ime, prezime, broj, role, activationToken, verifikacija);
        this.trainings = trainings;
    }

    public RegisteredUser(String korisnickoIme, String email, String password, String ime, String prezime, String broj, Role role, String activationToken, boolean verifikacija, Set<Training> trainings) {
        super(korisnickoIme, email, password, ime, prezime, broj, role, activationToken, verifikacija);
        this.trainings = trainings;
    }

    public RegisteredUser(String korisnickoIme, String email, String password, String ime, String prezime, String broj, Role role, boolean verifikacija, Set<Training> trainings) {
        super(korisnickoIme, email, password, ime, prezime, broj, role, verifikacija);
        this.trainings = trainings;
    }

    public RegisteredUser(Integer id, String korisnickoIme, String email, String password, String ime, String prezime, String broj, Role role, boolean verifikacija, Set<Training> trainings) {
        super(id, korisnickoIme, email, password, ime, prezime, broj, role, verifikacija);
        this.trainings = trainings;
    }
}
