package com.developer.onlybuns.repository;


import com.developer.onlybuns.entity.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Integer> {
    Trainer findByEmailAndPassword(String email, String password);
    Trainer findByEmail(String email);
    @Query("SELECT email FROM Trainer")
    List<String> findAllEmails();

}
