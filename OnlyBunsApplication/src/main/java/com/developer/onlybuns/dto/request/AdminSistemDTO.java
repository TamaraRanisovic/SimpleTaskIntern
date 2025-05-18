package com.developer.onlybuns.dto.request;

public class AdminSistemDTO {

    private String message;

    private String email;
    private String password;

    private String ime;
    private String prezime;

    public AdminSistemDTO() {}

    public AdminSistemDTO(String message, String email, String password, String ime, String prezime) {
        this.email = email;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }
}
