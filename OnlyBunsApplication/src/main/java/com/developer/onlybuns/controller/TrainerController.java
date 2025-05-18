package com.developer.onlybuns.controller;

import com.developer.onlybuns.dto.request.LoginDTO;
import com.developer.onlybuns.entity.Trainer;
import com.developer.onlybuns.service.TrainerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/trener")
public class TrainerController {

    private final TrainerService trainerService;



    public TrainerController(TrainerService trainerService) {
        this.trainerService = trainerService;
    }

    @GetMapping
    public List<Trainer> findAll() {
        return trainerService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Trainer> findById(@PathVariable("id") Integer id) {
        return trainerService.findById(id);
    }

    @PostMapping("/save")
    public Trainer saveAdminSistem(@RequestBody Trainer trainer) {
        return trainerService.saveTrener(trainer);
    }

    @PostMapping("/updatepassword")
    public ResponseEntity<String> updatePassword(@RequestBody LoginDTO loginDTO) {
        if (trainerService.updatePassword(loginDTO)) {
            return ResponseEntity.ok("{\"message\": \"Uspesna promena lozinke.\"}");
        } else {
            return ResponseEntity.status(401).body("Proverite email i lozinku.");
        }
    }



    @DeleteMapping("/{id}")
    public void deleteTrener(@PathVariable("id") Integer id) {
        trainerService.deleteTrener(id);
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> emails = trainerService.getAllEmails();
        return ResponseEntity.ok(emails);
    }


}
