package com.developer.onlybuns.entity;



import com.developer.onlybuns.enums.Uloga;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.InheritanceType.TABLE_PER_CLASS;

@Entity
@Table(name = "korisnik")
@Inheritance(strategy=TABLE_PER_CLASS)
public class Korisnik {

    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    private Integer id;

    @Column(name="korisnickoIme", unique=true, nullable=false)
    private String korisnickoIme;

    @Column(name="email", unique=true, nullable=false)
    private String email;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="ime", nullable=false)
    private String ime;

    @Column(name="prezime", nullable=false)
    private String prezime;

    @Column(name="broj", nullable=false)
    private String  broj;

    @Enumerated(EnumType.STRING)
    @Column(name="uloga", nullable = false)
    private Uloga uloga;

    @Column(name="activationToken")
    private String activationToken;

    @Column(name="verifikacija", nullable = false)
    private boolean verifikacija;

    @Column(name="last_login", nullable=true)
    private LocalDateTime last_login;

    @ManyToOne
    @JoinColumn(name = "lokacija_id", nullable = false)
    private Lokacija lokacija;

    public Korisnik() {
    }

    public Korisnik(Integer id, String korisnickoIme, String email, String password, String ime, String prezime, String broj, Uloga uloga, String activationToken, boolean verifikacija, LocalDateTime last_login, Lokacija lokacija) {
        this.id = id;
        this.korisnickoIme = korisnickoIme;
        this.email = email;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.uloga = uloga;
        this.activationToken = activationToken;
        this.verifikacija = verifikacija;
        this.last_login = last_login;
        this.lokacija = lokacija;
    }

    public Korisnik(String korisnickoIme, String email, String password, String ime, String prezime, String broj, Uloga uloga, String activationToken, boolean verifikacija, LocalDateTime last_login, Lokacija lokacija) {
        this.korisnickoIme = korisnickoIme;
        this.email = email;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.uloga = uloga;
        this.activationToken = activationToken;
        this.verifikacija = verifikacija;
        this.last_login = last_login;
        this.lokacija = lokacija;
    }

    public Korisnik(String korisnickoIme, String email, String password, String ime, String prezime, String broj, Uloga uloga, String activationToken, boolean verifikacija, Lokacija lokacija) {
        this.korisnickoIme = korisnickoIme;
        this.email = email;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.uloga = uloga;
        this.activationToken = activationToken;
        this.verifikacija = verifikacija;
        this.lokacija = lokacija;
    }

    public Korisnik(String korisnickoIme, String email, String password, String ime, String prezime, String broj, Uloga uloga, boolean verifikacija, Lokacija lokacija) {
        this.korisnickoIme = korisnickoIme;
        this.email = email;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.uloga = uloga;
        this.verifikacija = verifikacija;
        this.lokacija = lokacija;
    }

    public Korisnik(Integer id, String korisnickoIme, String email, String password, String ime, String prezime, String broj, Uloga uloga, boolean verifikacija, Lokacija lokacija) {
        this.id = id;
        this.korisnickoIme = korisnickoIme;
        this.email = email;
        this.password = password;
        this.ime = ime;
        this.prezime = prezime;
        this.broj = broj;
        this.uloga = uloga;
        this.verifikacija = verifikacija;
        this.lokacija = lokacija;
    }

    public boolean isVerifikacija() {
        return verifikacija;
    }

    public void setVerifikacija(boolean verifikacija) {
        this.verifikacija = verifikacija;
    }

    public String getActivationToken() {
        return activationToken;
    }

    public void setActivationToken(String activationToken) {
        this.activationToken = activationToken;
    }

    public Uloga getUloga() {
        return uloga;
    }

    public void setUloga(Uloga uloga) {
        this.uloga = uloga;
    }
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getBroj() {
        return broj;
    }

    public void setBroj(String broj) {
        this.broj = broj;
    }

    public String getKorisnickoIme() {
        return korisnickoIme;
    }

    public void setKorisnickoIme(String korisnickoIme) {
        this.korisnickoIme = korisnickoIme;
    }

    public LocalDateTime getLast_login() {
        return last_login;
    }

    public void setLast_login(LocalDateTime last_login) {
        this.last_login = last_login;
    }

    public Lokacija getLokacija() {
        return lokacija;
    }

    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }
}