package com.developer.onlybuns.service;


import com.developer.onlybuns.dto.request.LokacijaInfoDTO;
import com.developer.onlybuns.entity.Lokacija;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Optional;

public interface LokacijaService {

    @Cacheable(value = "lokacijaCache", key = "'findAll'")
    List<Lokacija> findAll();

    @Cacheable(value = "lokacijaCache", key = "'findById_' + #id")
    Optional<Lokacija> findById(Integer id);

    @CacheEvict(value = "lokacijaCache", allEntries = true)
    Lokacija saveLokacija(Lokacija lokacija);

    @CacheEvict(value = "lokacijaCache", allEntries = true)
    Lokacija updateLokacija(Lokacija lokacija);

    

    Lokacija findByAddress(String ulica, String grad, String drzava);

    @CacheEvict(value = "lokacijaCache", allEntries = true)
    void removeFromCache();

    List<String> getLokacijaUsernames(Lokacija lokacija);
}
