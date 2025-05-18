package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.dto.request.LoginDTO;
import com.developer.onlybuns.entity.Trainer;
import com.developer.onlybuns.repository.TrenerRepository;
import com.developer.onlybuns.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrainerServiceImpl implements TrainerService {
    @Autowired
    private final TrenerRepository trenerRepository;

    public TrainerServiceImpl(TrenerRepository trenerRepository) {
        this.trenerRepository = trenerRepository;
    }

    public Optional<Trainer> findById(Integer id) {
        return trenerRepository.findById(id);
    }

    public List<Trainer> findAll() {
        return trenerRepository.findAll();
    }

    public Trainer saveTrener(Trainer adminSistem) {
        return trenerRepository.save(adminSistem);
    }

    public Trainer findByEmailAndPassword(String email, String password) {
        return trenerRepository.findByEmailAndPassword(email, password);
    }
    public void deleteTrener(Integer id) {
        trenerRepository.deleteById(id);
    }

    public boolean updatePassword(LoginDTO loginDTO) {
        Trainer trainer = trenerRepository.findByEmail(loginDTO.getEmail());

        if (trainer != null) {
            trainer.setPassword(loginDTO.getPassword());
            trenerRepository.save(trainer);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAllEmails() {
        return trenerRepository.findAllEmails();
    }


}
