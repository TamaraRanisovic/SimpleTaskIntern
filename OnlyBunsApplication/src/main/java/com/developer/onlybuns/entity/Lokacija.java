package com.developer.onlybuns.entity;


import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "lokacija")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Lokacija {

    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    private Integer id;

    @Column(name = "ulica", nullable = true)
    private String ulica;

    @Column(name = "grad", nullable = true)
    private String grad;

    @Column(name = "drzava", nullable = true)
    private String drzava;

    @Column(name = "g_sirina", nullable = false)
    private Double g_sirina;  // To store latitude coordinate

    @Column(name = "g_duzina", nullable = false)
    private Double g_duzina;

    @OneToMany(mappedBy = "lokacija", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Objava> objave;


    @OneToMany(mappedBy = "lokacija", cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Korisnik> korisnici;

    public Lokacija() {
    }

    public Lokacija(Lokacija lokacija) {
        this.id = lokacija.getId();
        this.ulica = lokacija.getUlica();
        this.grad = lokacija.getGrad();
        this.drzava = lokacija.getDrzava();
        this.g_sirina = lokacija.getG_sirina();
        this.g_duzina = lokacija.getG_duzina();
        this.objave = lokacija.getObjave();
        this.korisnici = lokacija.getKorisnici();
    }

    public Lokacija(Integer id, String ulica, String grad, String drzava, Double g_sirina, Double g_duzina, List<Objava> objave, List<Korisnik> korisnici) {
        this.id = id;
        this.ulica = ulica;
        this.grad = grad;
        this.drzava = drzava;
        this.g_sirina = g_sirina;
        this.g_duzina = g_duzina;
        this.objave = objave;
        this.korisnici = korisnici;
    }

    public Lokacija(String ulica, String grad, String drzava, Double g_sirina, Double g_duzina, List<Objava> objave, List<Korisnik> korisnici) {
        this.ulica = ulica;
        this.grad = grad;
        this.drzava = drzava;
        this.g_sirina = g_sirina;
        this.g_duzina = g_duzina;
        this.objave = objave;
        this.korisnici = korisnici;
    }

    public Lokacija(String ulica, String grad, String drzava, Double g_sirina, Double g_duzina) {
        this.ulica = ulica;
        this.grad = grad;
        this.drzava = drzava;
        this.g_sirina = g_sirina;
        this.g_duzina = g_duzina;
        this.objave = new ArrayList<Objava>();
        this.korisnici = new ArrayList<Korisnik>();
    }

    public Lokacija(String ulica, String grad, String drzava) {
        this.ulica = ulica;
        this.grad = grad;
        this.drzava = drzava;
        this.objave = new ArrayList<Objava>();
        this.korisnici = new ArrayList<Korisnik>();
    }

    public Lokacija(Double g_sirina, Double g_duzina) {
        this.g_sirina = g_sirina;
        this.g_duzina = g_duzina;
        this.objave = new ArrayList<Objava>();
        this.korisnici = new ArrayList<Korisnik>();
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

    public List<Objava> getObjave() {
        return objave;
    }

    public void setObjave(List<Objava> objave) {
        this.objave = objave;
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(List<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }
}