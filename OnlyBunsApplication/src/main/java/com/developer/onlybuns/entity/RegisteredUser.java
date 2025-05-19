package com.developer.onlybuns.entity;


import com.developer.onlybuns.enums.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="registered_user")
public class RegisteredUser extends User {


    @ManyToMany
    @JoinTable(
            name = "booked_trainings",
            joinColumns = @JoinColumn(name = "registered_user_id"),
            inverseJoinColumns = @JoinColumn(name = "training_id")
    )
    private Set<Training> trainings;



    public RegisteredUser() {
        this.trainings = new HashSet<Training>();
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

    public Set<Training> getTrainings() {
        return trainings;
    }

    public void setTrainings(Set<Training> trainings) {
        this.trainings = trainings;
    }
}
