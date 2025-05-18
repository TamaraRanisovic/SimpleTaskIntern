package com.developer.onlybuns.service;

import com.developer.onlybuns.entity.User;

public interface KorisnikService {
    User findByEmailAndPassword(String email, String password);

    User findByEmail(String email);
    String getKorisnikUloga(String email);

}