package com.developer.onlybuns.repository;


import com.developer.onlybuns.entity.AdminSistem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminSistemRepository extends JpaRepository<AdminSistem, Integer> {
    AdminSistem findByEmailAndPassword(String email, String password);
    AdminSistem findByEmail(String email);
    @Query("SELECT email FROM AdminSistem")
    List<String> findAllEmails();

}
