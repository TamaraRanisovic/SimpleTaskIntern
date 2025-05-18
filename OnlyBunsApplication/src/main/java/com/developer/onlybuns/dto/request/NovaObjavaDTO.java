package com.developer.onlybuns.dto.request;

import com.developer.onlybuns.entity.Lokacija;

import java.time.LocalDateTime;
import java.util.List;

public class NovaObjavaDTO {

    private String opis;

    private String slika;

    private LokacijaDTO lokacijaDTO;

    private LocalDateTime datum_objave;

    private String korisnicko_ime;


    public NovaObjavaDTO() {
    }

    public NovaObjavaDTO(String opis, String slika, LokacijaDTO lokacijaDTO, LocalDateTime datum_objave, String korisnicko_ime) {
        this.opis = opis;
        this.slika = slika;
        this.lokacijaDTO = lokacijaDTO;
        this.datum_objave = datum_objave;
        this.korisnicko_ime = korisnicko_ime;
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
}
