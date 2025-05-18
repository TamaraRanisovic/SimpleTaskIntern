package com.developer.onlybuns.dto.request;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import java.util.List;


public class LokacijaInfoDTO {
    private Integer id;

    private String ulica;

    private String grad;

    private String drzava;

    private Double g_sirina;

    private Double g_duzina;

    private List<String> korisnici;

    private List<ObjavaDTO> objavaDTOs;

    public LokacijaInfoDTO(){}

    public LokacijaInfoDTO(Integer id, String ulica, String grad, String drzava, Double g_sirina, Double g_duzina, List<String> korisnici, List<ObjavaDTO> objavaDTOs) {
        this.id = id;
        this.ulica = ulica;
        this.grad = grad;
        this.drzava = drzava;
        this.g_sirina = g_sirina;
        this.g_duzina = g_duzina;
        this.korisnici = korisnici;
        this.objavaDTOs = objavaDTOs;
    }

    public LokacijaInfoDTO(String ulica, String grad, String drzava, Double g_sirina, Double g_duzina, List<String> korisnici, List<ObjavaDTO> objavaDTOs) {
        this.ulica = ulica;
        this.grad = grad;
        this.drzava = drzava;
        this.g_sirina = g_sirina;
        this.g_duzina = g_duzina;
        this.korisnici = korisnici;
        this.objavaDTOs = objavaDTOs;
    }

    public LokacijaInfoDTO(String ulica, String grad, String drzava, Double g_sirina, Double g_duzina) {
        this.ulica = ulica;
        this.grad = grad;
        this.drzava = drzava;
        this.g_sirina = g_sirina;
        this.g_duzina = g_duzina;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public String getGrad() {
        return grad;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public String getDrzava() {
        return drzava;
    }

    public void setDrzava(String drzava) {
        this.drzava = drzava;
    }

    public Double getG_sirina() {
        return g_sirina;
    }

    public void setG_sirina(Double g_sirina) {
        this.g_sirina = g_sirina;
    }

    public Double getG_duzina() {
        return g_duzina;
    }

    public void setG_duzina(Double g_duzina) {
        this.g_duzina = g_duzina;
    }

    public List<String> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<String> korisnici) {
        this.korisnici = korisnici;
    }

    public List<ObjavaDTO> getObjavaDTOs() {
        return objavaDTOs;
    }

    public void setObjavaDTOs(List<ObjavaDTO> objavaDTOs) {
        this.objavaDTOs = objavaDTOs;
    }
}
