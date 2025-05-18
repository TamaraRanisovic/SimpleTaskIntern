package com.developer.onlybuns.service;

import com.developer.onlybuns.dto.request.LoginDTO;
import com.developer.onlybuns.entity.AdminSistem;

import java.util.List;
import java.util.Optional;

public interface AdminSistemService {
    Optional<AdminSistem> findById(Integer id);
    List<AdminSistem> findAll();
    AdminSistem saveAdminSistem(AdminSistem adminSistem);
    void deleteAdminSistem(Integer id);
    boolean updatePassword(LoginDTO loginDTO);

    List<String> getAllEmails();
    AdminSistem findByEmailAndPassword(String email, String password);

}