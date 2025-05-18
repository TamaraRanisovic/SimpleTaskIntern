package com.developer.onlybuns.controller;

import com.developer.onlybuns.dto.request.AdminSistemDTO;
import com.developer.onlybuns.dto.request.JwtUtil;
import com.developer.onlybuns.dto.request.LoginDTO;
import com.developer.onlybuns.entity.AdminSistem;
import com.developer.onlybuns.entity.Objava;
import com.developer.onlybuns.rabbitmq.Producer;
import com.developer.onlybuns.service.AdminSistemService;
import com.developer.onlybuns.service.ObjavaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/adminsistem")
public class AdminSistemController {

    private final AdminSistemService adminSistemService;

    private final ObjavaService objavaService;

    private final Producer producer;


    public AdminSistemController(AdminSistemService adminSistemService, ObjavaService objavaService, Producer producer) {
        this.adminSistemService = adminSistemService;
        this.objavaService = objavaService;
        this.producer = producer;
    }

    @GetMapping
    public List<AdminSistem> findAll() {
        return adminSistemService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<AdminSistem> findById(@PathVariable("id") Integer id) {
        return adminSistemService.findById(id);
    }

    @PostMapping("/save")
    public AdminSistem saveAdminSistem(@RequestBody AdminSistem adminSistem) {
        return adminSistemService.saveAdminSistem(adminSistem);
    }

    @PostMapping("/updatepassword")
    public ResponseEntity<String> updatePassword(@RequestBody LoginDTO loginDTO) {
        if (adminSistemService.updatePassword(loginDTO)) {
            return ResponseEntity.ok("{\"message\": \"Uspesna promena lozinke.\"}");
        } else {
            return ResponseEntity.status(401).body("Proverite email i lozinku.");
        }
    }



    @PostMapping("/login")
    public ResponseEntity<Object> loginAdminSistem(@RequestBody LoginDTO loginDTO) {
        AdminSistem adminSistem = adminSistemService.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
        if (adminSistem != null) {
            AdminSistemDTO adminSistemDTO = new AdminSistemDTO();
            adminSistemDTO.setMessage("Uspesna prijava.");
            adminSistemDTO.setIme(adminSistem.getIme());
            adminSistemDTO.setPrezime(adminSistem.getPrezime());
            adminSistemDTO.setEmail(adminSistem.getEmail());

            return ResponseEntity.ok(adminSistemDTO);
            //return ResponseEntity.ok("{\"message\": \"Uspesna prijava.\"}");
        } else {
            return ResponseEntity.status(401).body("Neuspesna prijava. Proverite email i lozinku.");
        }
    }


    @DeleteMapping("/{id}")
    public void deleteAdminSistem(@PathVariable("id") Integer id) {
        adminSistemService.deleteAdminSistem(id);
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> emails = adminSistemService.getAllEmails();
        return ResponseEntity.ok(emails);
    }


    @PostMapping("/advertise")
    public ResponseEntity<String> sendPostsToAdvertisers(
            @RequestBody List<Integer> postIds,
            @RequestHeader("Authorization") String authHeader
    ) {
        // 1. Check token format
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing or invalid Authorization header.");
        }

        // 2. Extract token
        String token = authHeader.substring(7); // Remove "Bearer "

        // 3. Validate token
        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid JWT token.");
        }

        // 4. Extract user role from token
        String role = JwtUtil.getRoleFromToken(token);
        if (!"ADMIN_SISTEMA".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied. Only registered users can advertise posts.");
        }

        // 5. Process the posts
        for (Integer postId : postIds) {
            Optional<Objava> postOpt = objavaService.getById(postId);

            if (postOpt.isPresent()) {
                Objava post = postOpt.get();
                String message = String.format("Opis: %s\nVreme objave: %s\nKorisnik: %s",
                        post.getOpis(),
                        post.getDatum_objave().toString(),
                        post.getRegistrovaniKorisnik().getKorisnickoIme()
                );
                producer.sendFanout("advertisingExchange", message);
            }
        }

        return ResponseEntity.ok("Successfully sent selected posts to advertising apps.");
    }


}
