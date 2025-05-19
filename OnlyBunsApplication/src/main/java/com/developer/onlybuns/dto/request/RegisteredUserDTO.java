package com.developer.onlybuns.dto.request;

import javax.persistence.Column;

public class RegisteredUserDTO {
    private String username;

    private String email;

    private String name;

    private String surname;

    private String phoneNumber;

    public RegisteredUserDTO() {
    }

    public RegisteredUserDTO(String username, String email, String name, String surname, String phoneNumber) {
        this.username = username;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
