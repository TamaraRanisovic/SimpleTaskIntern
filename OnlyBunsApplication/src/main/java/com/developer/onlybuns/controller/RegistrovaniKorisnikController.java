package com.developer.onlybuns.controller;
import com.developer.onlybuns.dto.request.JwtUtil;
import com.developer.onlybuns.dto.request.RegistrovaniKorisnikDTO;
import com.developer.onlybuns.dto.request.SevenDaysReportDTO;
import com.developer.onlybuns.entity.Lokacija;
import com.developer.onlybuns.entity.Pratioci;
import com.developer.onlybuns.entity.RegistrovaniKorisnik;
import com.developer.onlybuns.service.ObjavaService;
import com.developer.onlybuns.service.RegistrovaniKorisnikService;
import com.developer.onlybuns.service.UsernameValidationService;
import com.developer.onlybuns.service.impl.RegistrovaniKorisnikImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/registrovaniKorisnik")
public class RegistrovaniKorisnikController {

    @Autowired
    private JavaMailSender mailSender;

    private final RegistrovaniKorisnikService registrovaniKorisnikService;

    private final ObjavaService objavaService;

    private final UsernameValidationService usernameValidationService;

    public RegistrovaniKorisnikController(RegistrovaniKorisnikService registrovaniKorisnikService, ObjavaService objavaService, UsernameValidationService usernameValidationService) {
        this.registrovaniKorisnikService = registrovaniKorisnikService;
        this.usernameValidationService = usernameValidationService;
        this.objavaService = objavaService;
    }

    @GetMapping
    public List<RegistrovaniKorisnik> findAllRegistrovaniKorisnik() {
        return registrovaniKorisnikService.findAllRegistrovaniKorisnik();
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegistrovaniKorisnik registrovaniKorisnik) {
        try {
            // Validate and save the user in an "inactive" state
            String activationToken = UUID.randomUUID().toString();
            registrovaniKorisnikService.register(registrovaniKorisnik, activationToken);

            // Send activation email
            sendActivationEmail(registrovaniKorisnik.getEmail(), activationToken);

            return ResponseEntity.ok("Registration successful. Please check your email to activate your account.");
        } catch (RegistrovaniKorisnikImpl.BadRequestException e) {
            // Handle specific error for invalid location
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }



    private void sendActivationEmail(String email, String token) {
        String activationLink = "http://localhost:8080/registrovaniKorisnik/activate?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ranisovic.in1.2020@uns.ac.rs");
        message.setTo(email);
        message.setSubject("Activate your account");
        message.setText("Click the following link to activate your account: " + activationLink);
        mailSender.send(message);
    }
/*

    @GetMapping("/send-report")
    public ResponseEntity<?> sendReportEmail(@RequestParam String username) {
        Optional<RegistrovaniKorisnik> registrovaniKorisnik = registrovaniKorisnikService.findByUsername(username);
        if (registrovaniKorisnik != null) {
            SevenDaysReportDTO sevenDaysReportDTO = registrovaniKorisnikService.generateSevenDaysReport(username, registrovaniKorisnik.get().getLast_login().toString());
            sendSevenDaysReportEmail(registrovaniKorisnik.get().getEmail(), registrovaniKorisnik.get().getKorisnickoIme(), sevenDaysReportDTO);
            return ResponseEntity.ok("Report successfully sent.");
        }
        return ResponseEntity.badRequest().body("User doesn't exist.");


    }
*/


    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        boolean isActivated = registrovaniKorisnikService.activateAccount(token);
        if (isActivated) {
            return ResponseEntity.ok("Account activated successfully. You can now log in.");
        } else {
            return ResponseEntity.badRequest().body("Invalid activation token.");
        }
    }

    @GetMapping("/{id}")
    public Optional<RegistrovaniKorisnik> findRegistrovaniKorisnikById(@PathVariable("id") Integer id) {
        return registrovaniKorisnikService.findById(id);
    }

    @GetMapping("/username/{username}")
    public RegistrovaniKorisnikDTO getKorisnikDTOByUsername(@PathVariable("username") String username) {
        return registrovaniKorisnikService.getKorisnikDTOByUsername(username);
    }

    @GetMapping("/followers/{username}")
    public List<String> getAllFollowers(@PathVariable("username") String username) {
        return registrovaniKorisnikService.getAllFollowers(username);
    }

    @GetMapping("/following/{username}")
    public List<String> getAllFollowing(@PathVariable("username") String username) {
        return registrovaniKorisnikService.getAllFollowing(username);
    }

    @PostMapping("/add")
    public RegistrovaniKorisnik saveRegistrovaniKorisnik(@RequestBody RegistrovaniKorisnik registrovaniKorisnik) {
        return registrovaniKorisnikService.saveRegistrovaniKorisnik(registrovaniKorisnik);
    }

    @PostMapping("/login")
    public ResponseEntity<String> prijaviKorisnika(@RequestBody RegistrovaniKorisnik korisnik) {
        RegistrovaniKorisnik validCredentials = registrovaniKorisnikService.proveriKorisnika(korisnik.getEmail(), korisnik.getPassword());
        if (validCredentials != null) {
            return ResponseEntity.ok("{\"message\": \"Uspesna prijava.\"}");
        } else {
            return ResponseEntity.status(401).body("Neuspešna prijava. Proverite email i lozinku.");
        }
    }

    @PutMapping
    public RegistrovaniKorisnik updateRegistrovaniKorisnik(@RequestBody RegistrovaniKorisnik employeeEntity) {
        return registrovaniKorisnikService.updateRegistrovaniKorisnik(employeeEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteRegistrovaniKorisnik(@PathVariable("id") Integer id) {
        registrovaniKorisnikService.deleteRegistrovaniKorisnik(id);
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> emails = registrovaniKorisnikService.getAllEmails();
        return ResponseEntity.ok(emails);
    }


    @GetMapping("/username")
    public ResponseEntity<List<String>> getAllUsernames() {

        List<String> usernames = registrovaniKorisnikService.getAllUsernames();
        return ResponseEntity.ok(usernames);
    }


    @GetMapping("/check-username")
    public ResponseEntity<List<String>> checkUsername(@RequestParam String username) {
        // First check in the Bloom Filter
        usernameValidationService.loadUsernamesFromDatabase();
        List<String> exists = new ArrayList<String>();

        if (usernameValidationService.isUsernameValid(username)) {
            exists.add("true");
            // Username exists, return true for existence
            return ResponseEntity.ok(exists);
        }
        exists.add("false");
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/count-new")
    public ResponseEntity<?> countNewFollowers() {
        try {
            int newFollowers = registrovaniKorisnikService.getNewFollowersCount("user1", LocalDateTime.parse("2024-12-07T16:00:00"));
            return ResponseEntity.ok(newFollowers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/report")
    public ResponseEntity<?> sevenDaysReport(@RequestParam String username, @RequestParam String lastLogin) {
        try {
            int newFollowersCount = registrovaniKorisnikService.getNewFollowersCount(username, LocalDateTime.parse(lastLogin));
            int newCommentsCount = objavaService.countNewCommentsOnUserPosts(username, LocalDateTime.parse(lastLogin));
            int newLikesCount = objavaService.countNewLikesOnUserPosts(username, LocalDateTime.parse(lastLogin));

            SevenDaysReportDTO sevenDaysReportDTO = new SevenDaysReportDTO(newFollowersCount, newCommentsCount, newLikesCount);
            return ResponseEntity.ok(sevenDaysReportDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }


    @GetMapping("/lokacija")
    public ResponseEntity<double[]> getUserLocation(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        String token = authHeader.substring(7); // Remove "Bearer " prefix

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
        double[] coordinates = { lokacija.getG_sirina(), lokacija.getG_duzina() };
        return ResponseEntity.ok(coordinates);
    }





}
