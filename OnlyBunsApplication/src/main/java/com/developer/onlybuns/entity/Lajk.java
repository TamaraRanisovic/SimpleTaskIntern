package com.developer.onlybuns.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="lajk", uniqueConstraints = @UniqueConstraint(columnNames = {"korisnik_id", "objava_id"}))
public class Lajk {

    @Id
    @SequenceGenerator(name = "mySeqGenV1", sequenceName = "mySeqV1", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mySeqGenV1")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "korisnik_id", nullable = false)
    private RegistrovaniKorisnik registrovaniKorisnik;

    @ManyToOne
    @JoinColumn(name = "objava_id", nullable = false)
    private Objava objava;

    @Column(name="datum_lajkovanja", nullable=false)
    private LocalDateTime datum_lajkovanja;

    public Lajk() {

    }

    public Lajk(Integer id, RegistrovaniKorisnik registrovaniKorisnik, Objava objava, LocalDateTime datum_lajkovanja) {
        this.id = id;
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.objava = objava;
        this.datum_lajkovanja = datum_lajkovanja;
    }

    public Lajk(RegistrovaniKorisnik registrovaniKorisnik, Objava objava, LocalDateTime datum_lajkovanja) {
        this.registrovaniKorisnik = registrovaniKorisnik;
        this.objava = objava;
        this.datum_lajkovanja = datum_lajkovanja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public LocalDateTime getDatum_lajkovanja() {
        return datum_lajkovanja;
    }

    public void setDatum_lajkovanja(LocalDateTime datum_lajkovanja) {
        this.datum_lajkovanja = datum_lajkovanja;
    }
}
