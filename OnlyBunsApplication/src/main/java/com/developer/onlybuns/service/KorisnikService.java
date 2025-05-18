package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.Korisnik;

import java.time.LocalDateTime;

public interface KorisnikService {
    Korisnik findByEmailAndPassword(String email, String password);

    Korisnik findByEmail(String email);
    String getKorisnikUloga(String email);

    public void updateLastLogin(String email, LocalDateTime lastLogin);
}