package com.developer.onlybuns.service;

import com.developer.onlybuns.dto.request.LoginDTO;
import com.developer.onlybuns.entity.Trainer;

import java.util.List;
import java.util.Optional;

public interface TrainerService {
    Optional<Trainer> findById(Integer id);
    List<Trainer> findAll();
    Trainer saveTrener(Trainer adminSistem);
    void deleteTrener(Integer id);
    boolean updatePassword(LoginDTO loginDTO);

    List<String> getAllEmails();
    Trainer findByEmailAndPassword(String email, String password);

}