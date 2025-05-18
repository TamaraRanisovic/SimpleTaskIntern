package com.developer.onlybuns.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="komentar", uniqueConstraints = @UniqueConstraint(columnNames = {"korisnik_id", "objava_id"}))
public class Komentar {

    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    private Integer id;

    @Column(name="opis", unique=true, nullable=false)
    private String opis;

    @Column(name="datum_objave", nullable=false)
    private LocalDateTime datum_kreiranja;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private RegistrovaniKorisnik registrovaniKorisnik;

    @ManyToOne
    @JoinColumn(name = "objava_id", nullable = false)
    private Objava objava;


    public Komentar() {

    }

    public Komentar(Integer id, String opis, LocalDateTime datum_kreiranja, RegistrovaniKorisnik registrovaniKorisnik, Objava objava) {
        this.id = id;
        this.opis = opis;
        this.datum_kreiranja = datum_kreiranja;
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.objava = objava;
    }

    public Komentar(String opis, LocalDateTime datum_kreiranja, RegistrovaniKorisnik registrovaniKorisnik, Objava objava) {
        this.opis = opis;
        this.datum_kreiranja = datum_kreiranja;
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.objava = objava;
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

    public LocalDateTime getDatum_kreiranja() {
        return datum_kreiranja;
    }

    public void setDatum_kreiranja(LocalDateTime datum_kreiranja) {
        this.datum_kreiranja = datum_kreiranja;
    }

    public RegistrovaniKorisnik getRegistrovaniKorisnik() {
        return registrovaniKorisnik;
    }

    public void setRegistrovaniKorisnik(RegistrovaniKorisnik registrovaniKorisnik) {
        this.registrovaniKorisnik = registrovaniKorisnik;
    }

    public Objava getObjava() {
        return objava;
    }

    public void setObjava(Objava objava) {
        this.objava = objava;
    }
}
