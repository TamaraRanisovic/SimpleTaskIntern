package com.developer.onlybuns.service.impl;

import com.developer.onlybuns.dto.request.*;
import com.developer.onlybuns.entity.*;
import com.developer.onlybuns.enums.Uloga;
import com.developer.onlybuns.repository.LokacijaRepository;
import com.developer.onlybuns.repository.ObjavaRepository;
import com.developer.onlybuns.repository.RegistrovaniKorisnikRepository;
import com.developer.onlybuns.service.GeocodingService;
import com.developer.onlybuns.service.LokacijaService;
import com.developer.onlybuns.service.ObjavaService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class ObjavaServiceImpl implements ObjavaService {

    private final ObjavaRepository objavaRepository;

    private final LokacijaRepository lokacijaRepository;

    private final LokacijaService lokacijaService;

    private final GeocodingService geocodingService;

    private final Logger LOG = LoggerFactory.getLogger(ObjavaServiceImpl.class);



    public ObjavaServiceImpl(ObjavaRepository objavaRepository, LokacijaRepository lokacijaRepository, LokacijaService lokacijaService, GeocodingService geocodingService) {
        this.objavaRepository = objavaRepository;
        this.lokacijaRepository = lokacijaRepository;
        this.lokacijaService = lokacijaService;
        this.geocodingService = geocodingService;
    }

    public String getObjavaUsername(Integer id) {
        Optional<Objava> objava = getById(id);
        if (objava != null) {
            String username = objava.get().getRegistrovaniKorisnik().getKorisnickoIme();
            return username;
        } else {
            return null;
        }
    }

    public List<LajkDTO> getObjavaLajkoviDTO(Integer id){
        Optional<Objava> objava = getById(id);
        List<LajkDTO> lajkoviDTO = new ArrayList<LajkDTO>();
        if (objava != null) {
            List<Lajk> lajkovi = objava.get().getLajkovi();
            for (Lajk lajk : lajkovi) {
                LajkDTO lajkDTO = new LajkDTO(lajk.getId(), lajk.getRegistrovaniKorisnik().getEmail(), lajk.getDatum_lajkovanja(), lajk.getObjava().getId());
                lajkoviDTO.add(lajkDTO);
            }
            return lajkoviDTO;
        } else {
            return lajkoviDTO;
        }
    }

    public List<KomentarDTO> getObjavaKomentariiDTO(Integer id){
        Optional<Objava> objava = getById(id);
        List<KomentarDTO> komentariDTO = new ArrayList<KomentarDTO>();
        if (objava != null) {
            List<Komentar> komentari = objava.get().getKomentari();
            for (Komentar komentar : komentari) {
                KomentarDTO komentarDTO = new KomentarDTO(komentar.getId(), komentar.getOpis(), komentar.getRegistrovaniKorisnik().getKorisnickoIme(), komentar.getDatum_kreiranja(), komentar.getObjava().getId());
                komentariDTO.add(komentarDTO);
            }
            return komentariDTO;
        } else {
            return komentariDTO;
        }
    }

    @Override
    public List<ObjavaDTO> findAllObjavaDTO() {
        List<Objava> objave = objavaRepository.findAll();
        List<ObjavaDTO> objaveDTO = new ArrayList<ObjavaDTO>();
        for (Objava objava : objave) {
            String korisnicko_ime = getObjavaUsername(objava.getId());
            List<LajkDTO> lajkoviDTO = getObjavaLajkoviDTO(objava.getId());
            List<KomentarDTO> komentariDTO = getObjavaKomentariiDTO(objava.getId());
            Integer broj_lajkova = lajkoviDTO.size();
            Integer broj_komentara = komentariDTO.size();
            Lokacija lokacija = objava.getLokacija();
            LokacijaDTO lokacijaDTO = new LokacijaDTO(lokacija.getUlica(), lokacija.getGrad(), lokacija.getDrzava());
            ObjavaDTO objavaDTO = new ObjavaDTO(objava.getId(), objava.getOpis(), objava.getSlika(), lokacijaDTO, objava.getDatum_objave(), korisnicko_ime, komentariDTO, lajkoviDTO, broj_lajkova, broj_komentara);
            objaveDTO.add(objavaDTO);
        }
        objaveDTO.sort(Comparator.comparing(ObjavaDTO::getDatum_objave).reversed());

        return objaveDTO;
    }

    @Override
    public List<ObjavaDTO> findAllObjavaDTOByUser(String username) {
        List<Objava> objave = objavaRepository.findAll();
        List<ObjavaDTO> objaveDTO = new ArrayList<ObjavaDTO>();
        for (Objava objava : objave) {
            String korisnicko_ime = getObjavaUsername(objava.getId());
            if (korisnicko_ime.equals(username)) {
                List<LajkDTO> lajkoviDTO = getObjavaLajkoviDTO(objava.getId());
                List<KomentarDTO> komentariDTO = getObjavaKomentariiDTO(objava.getId());
                Integer broj_lajkova = lajkoviDTO.size();
                Integer broj_komentara = komentariDTO.size();
                Lokacija lokacija = objava.getLokacija();
                LokacijaDTO lokacijaDTO = new LokacijaDTO(lokacija.getUlica(), lokacija.getGrad(), lokacija.getDrzava());
                ObjavaDTO objavaDTO = new ObjavaDTO(objava.getId(), objava.getOpis(), objava.getSlika(), lokacijaDTO, objava.getDatum_objave(), korisnicko_ime, komentariDTO, lajkoviDTO, broj_lajkova, broj_komentara);
                objaveDTO.add(objavaDTO);
            }
        }
        return objaveDTO;
    }

    public int countNewCommentsOnUserPosts(String username, LocalDateTime fromDate) {
        // Step 1: Get all posts of the user
        List<ObjavaDTO> posts = findAllObjavaDTOByUser(username);

        // Step 2: Get all comments on those posts
        List<KomentarDTO> allComments = findCommentsOnPosts(posts);

        // Step 3: Filter the comments to find ones that are created since 'fromDate' and by other users
        List<KomentarDTO> newComments = allComments.stream()
                .filter(c -> !c.getKorisnicko_ime().equals(username) &&
                        c.getDatum_kreiranja().isAfter(fromDate))
                .collect(Collectors.toList());

        // Step 4: Return the count of those comments
        return newComments.size();
    }

    public int countNewLikesOnUserPosts(String username, LocalDateTime fromDate) {
        List<ObjavaDTO> posts = findAllObjavaDTOByUser(username);

        List<LajkDTO> allLikes = findLikesOnPosts(posts);

        List<LajkDTO> newLikes = allLikes.stream()
                .filter(c -> !c.getKorisnicko_ime().equals(username) &&
                        c.getDatum_lajkovanja().isAfter(fromDate))
                .collect(Collectors.toList());

        return newLikes.size();
    }

    public List<KomentarDTO> findCommentsOnPosts(List<ObjavaDTO> posts) {
        List<KomentarDTO> komentarDTOS = new ArrayList<KomentarDTO>();
        for (ObjavaDTO objavaDTO : posts) {
            komentarDTOS.addAll(objavaDTO.getKomentari());
        }
        return  komentarDTOS;
    }

    public List<LajkDTO> findLikesOnPosts(List<ObjavaDTO> posts) {
        List<LajkDTO> lajkDTOS = new ArrayList<LajkDTO>();
        for (ObjavaDTO objavaDTO : posts) {
            lajkDTOS.addAll(objavaDTO.getLajkovi());
        }
        return  lajkDTOS;
    }

    @Override
    public ObjavaDTO findById(Integer id) {
        Optional<Objava> objava = objavaRepository.findById(id);

        if (objava != null) {
            String korisnicko_ime = getObjavaUsername(id);
            List<LajkDTO> lajkoviDTO = getObjavaLajkoviDTO(objava.get().getId());
            List<KomentarDTO> komentariDTO = getObjavaKomentariiDTO(objava.get().getId());
            Integer broj_lajkova = lajkoviDTO.size();
            Integer broj_komentara = komentariDTO.size();
            Lokacija lokacija = objava.get().getLokacija();
            LokacijaDTO lokacijaDTO = new LokacijaDTO(lokacija.getUlica(), lokacija.getGrad(), lokacija.getDrzava());
            ObjavaDTO objavaDTO = new ObjavaDTO(objava.get().getId(), objava.get().getOpis(), objava.get().getSlika(), lokacijaDTO, objava.get().getDatum_objave(), korisnicko_ime, komentariDTO, lajkoviDTO, broj_lajkova, broj_komentara);
            return objavaDTO;
        } else {
            return null;
        }

    }



    @Override
    public Optional<Objava> getById(Integer id) {
        return objavaRepository.findById(id);
    }

    @Override
    public void saveObjava(NovaObjavaDTO novaObjavaDTO, RegistrovaniKorisnik registrovaniKorisnik) {
        Logger log = LoggerFactory.getLogger(getClass());

        log.info("Starting creating post with description: {}", novaObjavaDTO.getOpis());

        Objava novaObjava = new Objava();
        novaObjava.setOpis(novaObjavaDTO.getOpis());
        novaObjava.setSlika(novaObjavaDTO.getSlika());
        novaObjava.setDatum_objave(novaObjavaDTO.getDatum_objave());
        novaObjava.setRegistrovaniKorisnik(registrovaniKorisnik);

        LokacijaDTO lokacijaDTO = novaObjavaDTO.getLokacijaDTO();
        log.info("Checking if location exists: {}, {}, {}",
                lokacijaDTO.getUlica(),
                lokacijaDTO.getGrad(),
                lokacijaDTO.getDrzava());

        Lokacija lokacija = lokacijaService.findByAddress(
                lokacijaDTO.getUlica(),
                lokacijaDTO.getGrad(),
                lokacijaDTO.getDrzava()
        );

        if (lokacija == null) {
            log.info("Location not found, creating new location...");
            Lokacija novaLokacija = new Lokacija();

            novaLokacija.setUlica(lokacijaDTO.getUlica());
            novaLokacija.setGrad(lokacijaDTO.getGrad());
            novaLokacija.setDrzava(lokacijaDTO.getDrzava());

            double[] coordinates = geocodingService.getCoordinates(
                    lokacijaDTO.getUlica() + ", " + lokacijaDTO.getGrad() + ", " + lokacijaDTO.getDrzava()
            );

            novaLokacija.setG_sirina(coordinates[0]);
            novaLokacija.setG_duzina(coordinates[1]);

            // Save new location and flush
            lokacijaRepository.save(novaLokacija);
            lokacijaRepository.flush();
            log.info("New location saved with coordinates: {}, {}", coordinates[0], coordinates[1]);

            novaObjava.setLokacija(novaLokacija);

            log.info("Assigning post {} to location {}", novaObjava.getOpis(), novaLokacija.getId());

        } else {
            novaObjava.setLokacija(lokacija);

            log.info("Location found in database. Using existing location ID: {}", lokacija.getId());
            log.info("Assigning post {} to location {}", novaObjava.getOpis(), lokacija.getId());

        }

        objavaRepository.save(novaObjava);
        log.info("Post successfully saved: {}", novaObjava.getOpis());

    }

    @Override
    public Objava updateObjava(Objava objava) {
        return objavaRepository.save(objava);
    }

    @Override
    public void deleteObjava(Integer id) {
        objavaRepository.deleteById(id);
    }

    @Override
    public List<Lajk> getAllLajkovi(Integer id) {
        Optional<Objava> objava = getById(id);
        List<Lajk> lajkovi = new ArrayList<>();
        if (objava != null) {
            lajkovi = objava.get().getLajkovi();
            return lajkovi;
        } else {
            return lajkovi;
        }
    }

    @Override
    public List<Komentar> getAllKomentari(Integer id) {
        Optional<Objava> objava = getById(id);
        List<Komentar> komentari = new ArrayList<>();
        if (objava != null) {
            komentari = objava.get().getKomentari();
            return komentari;
        } else {
            return komentari;
        }
    }

    public List<ObjavaDTO> getLokacijaObjaveDTO(Lokacija lokacija) {
        List<ObjavaDTO> objaveDTO = new ArrayList<ObjavaDTO>();
        List<Objava> objave = lokacija.getObjave();
        for (Objava objava : objave) {
            ObjavaDTO objavaDTO = findById(objava.getId());
            objaveDTO.add(objavaDTO);
        }
        return objaveDTO;
    }

    @Override
    public List<LokacijaInfoDTO> findAllLokacijaInfoDTO() {
        List<Lokacija> lokacije = lokacijaService.findAll();
        List<LokacijaInfoDTO> lokacijaInfoDTOs = new ArrayList<LokacijaInfoDTO>();
        for (Lokacija lokacija : lokacije) {
            List<String> usernames = lokacijaService.getLokacijaUsernames(lokacija);
            List<ObjavaDTO> objaveDTO = getLokacijaObjaveDTO(lokacija);

            LokacijaInfoDTO lokacijaInfoDTO = new LokacijaInfoDTO(lokacija.getId(), lokacija.getUlica(), lokacija.getGrad(), lokacija.getDrzava(), lokacija.getG_sirina(), lokacija.getG_duzina(), usernames, objaveDTO);
            lokacijaInfoDTOs.add(lokacijaInfoDTO);
        }

        return lokacijaInfoDTOs;

    }


    @Override
    public LokacijaInfoDTO findLokacijaInfoDTOById(Integer id) {
        Optional<Lokacija> lokacija = lokacijaService.findById(id);
        if (lokacija != null) {
            Lokacija pronadjenaLokacija = lokacija.get();
            List<String> usernames = lokacijaService.getLokacijaUsernames(pronadjenaLokacija);
            List<ObjavaDTO> objaveDTO = getLokacijaObjaveDTO(pronadjenaLokacija);

            LokacijaInfoDTO lokacijaInfoDTO = new LokacijaInfoDTO(pronadjenaLokacija.getId(), pronadjenaLokacija.getUlica(), pronadjenaLokacija.getGrad(), pronadjenaLokacija.getDrzava(), pronadjenaLokacija.getG_sirina(), pronadjenaLokacija.getG_duzina(), usernames, objaveDTO);
            return lokacijaInfoDTO;
        } else {
            return null;
        }

    }

    @Override
    public List<ObjavaDTO> getUsersNearbyPosts(String username, double g_sirina, double g_duzina) {
        List<ObjavaDTO> allObjavaDTO = findAllObjavaDTO();
        List<ObjavaDTO> nearbyPosts = new ArrayList<ObjavaDTO>();
        for (ObjavaDTO objavaDTO : allObjavaDTO) {
            LokacijaDTO lokacijaDTO = objavaDTO.getLokacijaDTO();
            Lokacija lokacija = lokacijaService.findByAddress(lokacijaDTO.getUlica(), lokacijaDTO.getGrad(), lokacijaDTO.getDrzava());
            double distance = geocodingService.calculateDistance(g_sirina, g_duzina, lokacija.getG_sirina(), lokacija.getG_duzina());
            if (distance <= 10 && !objavaDTO.getKorisnicko_ime().equals(username)) {
                nearbyPosts.add(objavaDTO);
            }
        }
        return nearbyPosts;

    }

/*
    @Override
    public List<LokacijaInfoDTO> getUsersNearbyPosts(String username, double g_sirina, double g_duzina) {
    List<Lokacija> lokacije = lokacijaService.findAll();
    List<LokacijaInfoDTO> lokacijaInfoDTOs = new ArrayList<LokacijaInfoDTO>();
        for (Lokacija lokacija : lokacije) {
        double distance = geocodingService.calculateDistance(g_sirina, g_duzina, lokacija.getG_sirina(), lokacija.getG_duzina());
        if (distance <= 10) {
            LokacijaInfoDTO lokacijaInfoDTO = findLokacijaInfoDTOById(lokacija.getId());
            for (ObjavaDTO objavaDTO : lokacijaInfoDTO.getObjavaDTOs()) {
                if (!objavaDTO.getKorisnicko_ime().equals(username)) {
                    lokacijaInfoDTOs.add(lokacijaInfoDTO);
                }
            }
        }
    }
        return lokacijaInfoDTOs;

}*/
}