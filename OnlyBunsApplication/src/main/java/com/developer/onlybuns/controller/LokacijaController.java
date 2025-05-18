package com.developer.onlybuns.controller;

import com.developer.onlybuns.dto.request.*;
import com.developer.onlybuns.entity.Lokacija;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.service.GeocodingService;
import com.developer.onlybuns.service.LokacijaService;
import com.developer.onlybuns.service.ObjavaService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/lokacija")
public class LokacijaController {

    private final LokacijaService lokacijaService;

    private final ObjavaService objavaService;

    private final RegistrovaniKorisnikService registrovaniKorisnikService;

    private final GeocodingService geocodingService;


    public LokacijaController(LokacijaService lokacijaService, ObjavaService objavaService, RegistrovaniKorisnikService registrovaniKorisnikService, GeocodingService geocodingService) {
        this.lokacijaService = lokacijaService;
        this.objavaService = objavaService;
        this.registrovaniKorisnikService = registrovaniKorisnikService;
        this.geocodingService = geocodingService;
    }

    @GetMapping
    public List<LokacijaInfoDTO> findAllLokacijaInfoDTO() {
        return objavaService.findAllLokacijaInfoDTO();
    }

    @GetMapping("/{id}")
    public  ResponseEntity<LokacijaInfoDTO> findLokacijaInfoDTOById(@PathVariable("id") Integer id) {
        LokacijaInfoDTO lokacijaInfoDTO = objavaService.findLokacijaInfoDTOById(id);
        if (lokacijaInfoDTO != null) {
            return ResponseEntity.ok(lokacijaInfoDTO);
        } else {
            return (ResponseEntity<LokacijaInfoDTO>) ResponseEntity.notFound();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> save(@RequestBody Lokacija lokacija) {
            lokacijaService.saveLokacija(lokacija);
            return ResponseEntity.ok("{\"message\": \"Uspesno kreirana nova lokacija.\"}");
    }

    @PutMapping
    public Lokacija update(@RequestBody Lokacija lokacija) {
        return lokacijaService.updateLokacija(lokacija);
    }



    @GetMapping(value = "/removeCache")
    public ResponseEntity<String> removeFromCache() {
        lokacijaService.removeFromCache();
        return ResponseEntity.ok("Posts successfully removed from cache!");
    }


    @GetMapping("/nearby-posts")
    public ResponseEntity<List<Object[]>> getNearbyPosts(@RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.substring(7);

        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String username = JwtUtil.getUsernameFromToken(token);
        String role = JwtUtil.getRoleFromToken(token);

        if (!"REGISTROVANI_KORISNIK".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        Optional<RegistrovaniKorisnik> registrovaniKorisnik = registrovaniKorisnikService.findByUsername(username);

        if (!registrovaniKorisnik.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Lokacija lokacija = registrovaniKorisnik.get().getLokacija();
        List<ObjavaDTO> nearbyPosts = objavaService.getUsersNearbyPosts(username, lokacija.getG_sirina(), lokacija.getG_duzina());

        if (nearbyPosts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        List<Object[]> nearbyPostsInfo = new ArrayList<>();

        for (ObjavaDTO objavaDTO : nearbyPosts) {
            LokacijaDTO lokacijaDTO = objavaDTO.getLokacijaDTO();
            String address = lokacijaDTO.getUlica() + ", " + lokacijaDTO.getGrad() + ", " + lokacijaDTO.getDrzava();
            double[] coordinates = geocodingService.getCoordinates(address);

            if (coordinates != null) {
                Object[] data = {
                        coordinates[0],  // Latitude
                        coordinates[1],  // Longitude
                        objavaDTO.getId(),  // Post ID
                        objavaDTO.getOpis(),  // Post Description
                        objavaDTO.getKorisnicko_ime(),   // Username
                        objavaDTO.getSlika()   // Image path
                };
                nearbyPostsInfo.add(data);
            }
        }

        return ResponseEntity.ok(nearbyPostsInfo);
    }
}





