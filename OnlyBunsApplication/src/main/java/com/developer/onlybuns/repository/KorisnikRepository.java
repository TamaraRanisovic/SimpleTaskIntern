package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Korisnik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KorisnikRepository extends JpaRepository<Korisnik, Integer> {

    Korisnik findByEmailAndPassword(String email, String password);
    Korisnik findByEmail(String email);

}