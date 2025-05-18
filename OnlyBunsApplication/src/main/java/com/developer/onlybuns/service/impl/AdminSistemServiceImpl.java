package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.dto.request.LoginDTO;
import com.developer.onlybuns.entity.AdminSistem;
import com.developer.onlybuns.repository.AdminSistemRepository;
import com.developer.onlybuns.service.AdminSistemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminSistemServiceImpl implements AdminSistemService {
    @Autowired
    private final AdminSistemRepository adminSistemRepository;

    public AdminSistemServiceImpl(AdminSistemRepository adminSistemRepository) {
        this.adminSistemRepository = adminSistemRepository;
    }

    public Optional<AdminSistem> findById(Integer id) {
        return adminSistemRepository.findById(id);
    }

    public List<AdminSistem> findAll() {
        return adminSistemRepository.findAll();
    }

    public AdminSistem saveAdminSistem(AdminSistem adminSistem) {
        return adminSistemRepository.save(adminSistem);
    }

    public AdminSistem findByEmailAndPassword(String email, String password) {
        return adminSistemRepository.findByEmailAndPassword(email, password);
    }
    public void deleteAdminSistem(Integer id) {
        adminSistemRepository.deleteById(id);
    }

    public boolean updatePassword(LoginDTO loginDTO) {
        AdminSistem adminSistem = adminSistemRepository.findByEmail(loginDTO.getEmail());

        if (adminSistem != null) {
            adminSistem.setPassword(loginDTO.getPassword());
            adminSistemRepository.save(adminSistem);
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAllEmails() {
        return adminSistemRepository.findAllEmails();
    }

    /*@Override
    public Optional<AdminSistem> findById(Integer id) {
        Optional<Korisnik> korisnikOptional = korisnikRepository.findById(id);
        return korisnikOptional.map(korisnik -> (AdminSistem) korisnik);
    }
*/


   /* @Override
    public List<AdminSistem> findAll() {
        List<Korisnik> korisnici = korisnikRepository.findAll();
        return korisnici.stream()
                .filter(AdminSistem.class::isInstance)
                .map(AdminSistem.class::cast)
                .collect(Collectors.toList());
    }*/




}
