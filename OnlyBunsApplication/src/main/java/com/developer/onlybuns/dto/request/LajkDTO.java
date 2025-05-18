package com.developer.onlybuns.dto.request;

import java.time.LocalDateTime;

public class LajkDTO {
    private Integer id;

    private String korisnicko_ime;

    private LocalDateTime datum_lajkovanja;

    private Integer objava_id;

    public LajkDTO() {
    }

    public LajkDTO(Integer id, String korisnicko_ime, Integer objava_id) {
        this.id = id;
        this.korisnicko_ime = korisnicko_ime;
        this.objava_id = objava_id;
    }

    public LajkDTO(String korisnicko_ime, Integer objava_id) {
        this.korisnicko_ime = korisnicko_ime;
        this.objava_id = objava_id;
    }

    public LajkDTO(String korisnicko_ime, LocalDateTime datum_lajkovanja, Integer objava_id) {
        this.korisnicko_ime = korisnicko_ime;
        this.datum_lajkovanja = datum_lajkovanja;
        this.objava_id = objava_id;
    }

    public LajkDTO(Integer id, String korisnicko_ime, LocalDateTime datum_lajkovanja, Integer objava_id) {
        this.id = id;
        this.korisnicko_ime = korisnicko_ime;
        this.datum_lajkovanja = datum_lajkovanja;
        this.objava_id = objava_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public Integer getObjava_id() {
        return objava_id;
    }

    public void setObjava_id(Integer objava_id) {
        this.objava_id = objava_id;
    }

    public LocalDateTime getDatum_lajkovanja() {
        return datum_lajkovanja;
    }

    public void setDatum_lajkovanja(LocalDateTime datum_lajkovanja) {
        this.datum_lajkovanja = datum_lajkovanja;
    }
}
