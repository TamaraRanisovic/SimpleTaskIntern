package com.developer.onlybuns.controller;
import com.developer.onlybuns.dto.request.JwtUtil;
import com.developer.onlybuns.dto.request.LokacijaInfoDTO;
import com.developer.onlybuns.dto.request.NovaObjavaDTO;
import com.developer.onlybuns.dto.request.ObjavaDTO;
import com.developer.onlybuns.entity.*;
import com.developer.onlybuns.service.ObjavaService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/objava")
public class ObjavaController {

    private final ObjavaService objavaService;

    private final RegistrovaniKorisnikService registrovaniKorisnikService;


    public ObjavaController(ObjavaService objavaService, RegistrovaniKorisnikService registrovaniKorisnikService) {
        this.objavaService = objavaService;
        this.registrovaniKorisnikService = registrovaniKorisnikService;
    }

    @GetMapping
    public List<ObjavaDTO> findAllObjavaDTO() {
        return objavaService.findAllObjavaDTO();
    }

    @GetMapping("/user/{username}")
    public List<ObjavaDTO> findAllObjavaDTOByUser(@PathVariable("username") String username) {
        return objavaService.findAllObjavaDTOByUser(username);
    }

    @GetMapping("/{id}")
    public  ResponseEntity<ObjavaDTO> findById(@PathVariable("id") Integer id) {
        ObjavaDTO objavaDTO = objavaService.findById(id);
        if (objavaDTO != null) {
            return ResponseEntity.ok(objavaDTO);
        } else {
            return (ResponseEntity<ObjavaDTO>) ResponseEntity.notFound();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> save(@RequestBody NovaObjavaDTO novaObjavaDTO, @RequestHeader("Authorization") String authHeader) {

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(401).body("Invalid token");
        }

        Optional<RegistrovaniKorisnik> registrovaniKorisnik = registrovaniKorisnikService.findByUsername(novaObjavaDTO.getKorisnicko_ime());


        String username = JwtUtil.getUsernameFromToken(token);
        String role = JwtUtil.getRoleFromToken(token);

        if (!novaObjavaDTO.getKorisnicko_ime().equals(username)) {
            return ResponseEntity.status(403).body("Access denied. Only registered users can post.");
        }

        if (!"REGISTROVANI_KORISNIK".equals(role)) {
            return ResponseEntity.status(403).body("Access denied. Only registered users can post.");
        }

        if (registrovaniKorisnik != null) {
            objavaService.saveObjava(novaObjavaDTO, registrovaniKorisnik.get());

            return ResponseEntity.ok("{\"message\": \"Uspesno kreiran novi post.\"}");
        } else {
            return ResponseEntity.status(401).body("Neuspešno kreiranje novog posta. Proverite unete podatke.");
        }

    }

    @PutMapping
    public Objava update(@RequestBody Objava objava) {
        return objavaService.updateObjava(objava);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        objavaService.deleteObjava(id);
    }

    @GetMapping("/lajkovi")
    public ResponseEntity<List<Lajk>> getAllLajkovi(Integer id) {
        List<Lajk> lajkovi = objavaService.getAllLajkovi(id);
        return ResponseEntity.ok(lajkovi);
    }

    @GetMapping("/komentari")
    public ResponseEntity<List<Komentar>> getAllKomentari(Integer id) {
        List<Komentar> komentari = objavaService.getAllKomentari(id);
        return ResponseEntity.ok(komentari);
    }

    @GetMapping("/feed")
    public ResponseEntity<?> findAllUserFollows(
            @RequestHeader("Authorization") String authHeader
    ) {
        // 1. Check for token format
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
        }

        // 2. Extract token
        String token = authHeader.substring(7);

        // 3. Validate token
        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token.");
        }

        // 4. Extract username from token
        String username = JwtUtil.getUsernameFromToken(token);
        String role = JwtUtil.getRoleFromToken(token);

        if (!"REGISTROVANI_KORISNIK".equals(role)) {
            return ResponseEntity.status(403).body("Access denied. Only registered users can post.");
        }

        // 5. Fetch and return user feed
        List<ObjavaDTO> feed = registrovaniKorisnikService.findAllUserFollows(username);
        return ResponseEntity.ok(feed);
    }


    @GetMapping("/count-newcomments")
    public ResponseEntity<?> countNewFollowers() {
        try {
            int newFollowers = objavaService.countNewCommentsOnUserPosts("user1", LocalDateTime.parse("2024-12-07T10:00:00"));
            return ResponseEntity.ok(newFollowers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/count-newlikes")
    public ResponseEntity<?> countNewLikes() {
        try {
            int newLikes = objavaService.countNewLikesOnUserPosts("user1", LocalDateTime.parse("2024-12-07T16:19:00"));
            return ResponseEntity.ok(newLikes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }





}
