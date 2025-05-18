package com.developer.onlybuns.entity;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Cache;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="objava")
public class Objava {

    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    private Integer id;

    @Column(name="opis", nullable=false)
    private String opis;

    @Column(name="slika", nullable=false)
    private String slika;

    @Column(name="datum_objave", nullable=false)
    private LocalDateTime datum_objave;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private RegistrovaniKorisnik registrovaniKorisnik;

    @ManyToOne
    @JoinColumn(name = "lokacija_id", nullable = false)
    private Lokacija lokacija;

    @OneToMany(mappedBy = "objava", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Komentar> komentari;

    @OneToMany(mappedBy = "objava", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Lajk> lajkovi;

    public Objava() {

    }

    public Objava(Integer id, String opis, String slika, LocalDateTime datum_objave, RegistrovaniKorisnik registrovaniKorisnik, Lokacija lokacija, List<Komentar> komentari, List<Lajk> lajkovi) {
        this.id = id;
        this.opis = opis;
        this.slika = slika;
        this.datum_objave = datum_objave;
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.lokacija = lokacija;
        this.komentari = komentari;
        this.lajkovi = lajkovi;
    }

    public Objava(String opis, String slika, LocalDateTime datum_objave, RegistrovaniKorisnik registrovaniKorisnik, Lokacija lokacija, List<Komentar> komentari, List<Lajk> lajkovi) {
        this.opis = opis;
        this.slika = slika;
        this.datum_objave = datum_objave;
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.lokacija = lokacija;
        this.komentari = komentari;
        this.lajkovi = lajkovi;
    }


    public Objava(String opis, String slika, LocalDateTime datum_objave, RegistrovaniKorisnik registrovaniKorisnik, Lokacija lokacija) {
        this.opis = opis;
        this.slika = slika;
        this.datum_objave = datum_objave;
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.lokacija = lokacija;
        this.komentari = new ArrayList<Komentar>();
        this.lajkovi = new ArrayList<Lajk>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getSlika() {
        return slika;
    }

    public void setSlika(String slika) {
        this.slika = slika;
    }

    public LocalDateTime getDatum_objave() {
        return datum_objave;
    }

    public void setDatum_objave(LocalDateTime datum_objave) {
        this.datum_objave = datum_objave;
    }

    public RegistrovaniKorisnik getRegistrovaniKorisnik() {
        return registrovaniKorisnik;
    }

    public void setRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik) {
        this.registrovaniKorisnik = registrovaniKorisnik;
    }

    public List<Komentar> getKomentari() {
        return komentari;
    }

    public void setKomentari(List<Komentar> komentari) {
        this.komentari = komentari;
    }

    public List<Lajk> getLajkovi() {
        return lajkovi;
    }

    public void setLajkovi(List<Lajk> lajkovi) {
        this.lajkovi = lajkovi;
    }

    public Lokacija getLokacija() {
        return lokacija;
    }

    public void setLokacija(Lokacija lokacija) {
        this.lokacija = lokacija;
    }
}
