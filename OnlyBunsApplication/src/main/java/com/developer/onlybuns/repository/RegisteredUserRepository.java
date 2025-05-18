package com.developer.onlybuns.repository;

import java.util.List;
import java.util.Optional;

import com.developer.onlybuns.entity.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser, Integer> {
    @Query("SELECT email FROM RegisteredUser")
    List<String> findAllEmails();

    @Query("SELECT username FROM RegisteredUser")
    List<String> findAllUsernames();
    
    RegisteredUser findByEmailAndPassword(String email, String password);

    Optional<RegisteredUser> findByUsername(String username);

    Optional<RegisteredUser> findByActivationToken(String activationToken);


    @Query("SELECT COUNT(*) FROM RegisteredUser r WHERE r.username = :username")
    Integer countByUsername(String username);

}


