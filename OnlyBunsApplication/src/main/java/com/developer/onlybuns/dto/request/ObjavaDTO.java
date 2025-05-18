package com.developer.onlybuns.dto.request;


import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Cacheable;
import java.time.LocalDateTime;
import java.util.List;

public class ObjavaDTO {

    private Integer id;

    private String opis;

    private String slika;

    private LokacijaDTO lokacijaDTO;

    private LocalDateTime datum_objave;

    private String korisnicko_ime;

    private List<KomentarDTO> komentari;

    private List<LajkDTO> lajkovi;

    private Integer broj_lajkova;

    private Integer broj_komentara;

    public ObjavaDTO() {
    }

    public ObjavaDTO(Integer id, String opis, String slika, LokacijaDTO lokacijaDTO, LocalDateTime datum_objave, String korisnicko_ime, List<KomentarDTO> komentari, List<LajkDTO> lajkovi, Integer broj_lajkova, Integer broj_komentara) {
        this.id = id;
        this.opis = opis;
        this.slika = slika;
        this.lokacijaDTO = lokacijaDTO;
        this.datum_objave = datum_objave;
        this.korisnicko_ime = korisnicko_ime;
        this.komentari = komentari;
        this.lajkovi = lajkovi;
        this.broj_lajkova = broj_lajkova;
        this.broj_komentara = broj_komentara;
    }

    public ObjavaDTO(String opis, String slika, LokacijaDTO lokacijaDTO, LocalDateTime datum_objave, String korisnicko_ime, List<KomentarDTO> komentari, List<LajkDTO> lajkovi, Integer broj_lajkova, Integer broj_komentara) {
        this.opis = opis;
        this.slika = slika;
        this.lokacijaDTO = lokacijaDTO;
        this.datum_objave = datum_objave;
        this.korisnicko_ime = korisnicko_ime;
        this.komentari = komentari;
        this.lajkovi = lajkovi;
        this.broj_lajkova = broj_lajkova;
        this.broj_komentara = broj_komentara;
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

    public LokacijaDTO getLokacijaDTO() {
        return lokacijaDTO;
    }

    public void setLokacijaDTO(LokacijaDTO lokacijaDTO) {
        this.lokacijaDTO = lokacijaDTO;
    }

    public LocalDateTime getDatum_objave() {
        return datum_objave;
    }

    public void setDatum_objave(LocalDateTime datum_objave) {
        this.datum_objave = datum_objave;
    }

    public String getKorisnicko_ime() {
        return korisnicko_ime;
    }

    public void setKorisnicko_ime(String korisnicko_ime) {
        this.korisnicko_ime = korisnicko_ime;
    }

    public List<KomentarDTO> getKomentari() {
        return komentari;
    }

    public void setKomentari(List<KomentarDTO> komentari) {
        this.komentari = komentari;
    }

    public List<LajkDTO> getLajkovi() {
        return lajkovi;
    }

    public void setLajkovi(List<LajkDTO> lajkovi) {
        this.lajkovi = lajkovi;
    }

    public Integer getBroj_lajkova() {
        return broj_lajkova;
    }

    public void setBroj_lajkova(Integer broj_lajkova) {
        this.broj_lajkova = broj_lajkova;
    }

    public Integer getBroj_komentara() {
        return broj_komentara;
    }

    public void setBroj_komentara(Integer broj_komentara) {
        this.broj_komentara = broj_komentara;
    }
}
