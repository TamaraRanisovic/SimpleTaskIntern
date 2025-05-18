package com.developer.onlybuns.repository;

import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ObjavaRepository extends JpaRepository<Objava, Integer> {

    Optional<Objava> findById(Integer id);

    List<Objava> findAll();
}