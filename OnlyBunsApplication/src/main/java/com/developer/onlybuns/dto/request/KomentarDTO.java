package com.developer.onlybuns.dto.request;

import java.time.LocalDateTime;

public class KomentarDTO {
    private Integer id;

    private String opis;

    private String korisnicko_ime;

    private LocalDateTime datum_kreiranja;

    private Integer objava_id;

    public KomentarDTO() {
    }

    public KomentarDTO(Integer id, String opis, String korisnicko_ime, LocalDateTime datum_kreiranja, Integer objava_id) {
        this.id = id;
        this.opis = opis;
        this.korisnicko_ime = korisnicko_ime;
        this.datum_kreiranja = datum_kreiranja;
        this.objava_id = objava_id;
    }

    public KomentarDTO(String opis, String korisnicko_ime, LocalDateTime datum_kreiranja, Integer objava_id) {
        this.opis = opis;
        this.korisnicko_ime = korisnicko_ime;
        this.datum_kreiranja = datum_kreiranja;
        this.objava_id = objava_id;
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

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public LocalDateTime getDatum_kreiranja() {
        return datum_kreiranja;
    }

    public void setDatum_kreiranja(LocalDateTime datum_kreiranja) {
        this.datum_kreiranja = datum_kreiranja;
    }

    public Integer getObjava_id() {
        return objava_id;
    }

    public void setObjava_id(Integer objava_id) {
        this.objava_id = objava_id;
    }
}
