package com.developer.onlybuns.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pratioci", uniqueConstraints = @UniqueConstraint(columnNames = {"following_id", "followed_id"}))
public class Pratioci {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "following_id", nullable = false)
    private RegistrovaniKorisnik following;

    @ManyToOne
    @JoinColumn(name = "followed_id", nullable = false)
    private RegistrovaniKorisnik followed;

    @Column(name="datum_pracenja", nullable=false)
    private LocalDateTime datum_pracenja;

    // Constructors, Getters, and Setters

    public Pratioci() {
    }

    public Pratioci(Integer id, RegistrovaniKorisnik following, RegistrovaniKorisnik followed, LocalDateTime datum_pracenja) {
        this.id = id;
        this.following = following;
        this.followed = followed;
        this.datum_pracenja = datum_pracenja;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public RegistrovaniKorisnik getFollowing() {
        return following;
    }

    public void setFollowing(RegistrovaniKorisnik following) {
        this.following = following;
    }

    public RegistrovaniKorisnik getFollowed() {
        return followed;
    }

    public void setFollowed(RegistrovaniKorisnik followed) {
        this.followed = followed;
    }

    public LocalDateTime getDatum_pracenja() {
        return datum_pracenja;
    }

    public void setDatum_pracenja(LocalDateTime datum_pracenja) {
        this.datum_pracenja = datum_pracenja;
    }
}