package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.dto.request.LoginDTO;
import com.developer.onlybuns.entity.Trainer;
import com.developer.onlybuns.repository.TrainerRepository;
import com.developer.onlybuns.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private final TrainerRepository trainerRepository;

    public TrainerServiceImpl(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }

    public Optional<Trainer> findById(Integer id) {
        return trainerRepository.findById(id);
    }

    public List<Trainer> findAll() {
        return trainerRepository.findAll();
    }

    public Trainer saveTrener(Trainer adminSistem) {
        return trainerRepository.save(adminSistem);
    }

    public Trainer findByEmailAndPassword(String email, String password) {
        return trainerRepository.findByEmailAndPassword(email, password);
    }
    public void deleteTrener(Integer id) {
        trainerRepository.deleteById(id);
    }

    public boolean updatePassword(LoginDTO loginDTO) {
        Trainer trainer = trainerRepository.findByEmail(loginDTO.getEmail());

        if (trainer != null) {
            trainer.setPassword(loginDTO.getPassword());
            trainerRepository.save(trainer);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAllEmails() {
        return trainerRepository.findAllEmails();
    }


}
