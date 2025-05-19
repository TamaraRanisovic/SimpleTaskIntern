package com.developer.onlybuns.controller;
import com.developer.onlybuns.entity.RegisteredUser;
import com.developer.onlybuns.service.RegisteredUserService;
import com.developer.onlybuns.service.UsernameValidationService;
import com.developer.onlybuns.service.impl.RegisteredUserImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/registeredUser")
public class RegisteredUserController {

    @Autowired
    private JavaMailSender mailSender;

    private final RegisteredUserService registeredUserService;


    private final UsernameValidationService usernameValidationService;

    public RegisteredUserController(RegisteredUserService registeredUserService, UsernameValidationService usernameValidationService) {
        this.registeredUserService = registeredUserService;
        this.usernameValidationService = usernameValidationService;
    }

    @GetMapping
    public List<RegisteredUser> findAllRegistrovaniKorisnik() {
        return registeredUserService.findAllRegistrovaniKorisnik();
    }


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisteredUser registeredUser) {
        try {
            // Validate and save the user in an "inactive" state
            String activationToken = UUID.randomUUID().toString();
            registeredUserService.register(registeredUser, activationToken);

            // Send activation email
            sendActivationEmail(registeredUser.getEmail(), activationToken);

            return ResponseEntity.ok("Registration successful. Please check your email to activate your account.");
        } catch (RegisteredUserImpl.BadRequestException e) {
            // Handle specific error for invalid location
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    private void sendActivationEmail(String email, String token) {
        String activationLink = "http://localhost:8080/registeredUser/activate?token=" + token;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ranisovic.in1.2020@uns.ac.rs");
        message.setTo(email);
        message.setSubject("Activate your account");
        message.setText("Click the following link to activate your account: " + activationLink);
        mailSender.send(message);
    }


    @GetMapping("/activate")
    public ResponseEntity<String> activateAccount(@RequestParam("token") String token) {
        boolean isActivated = registeredUserService.activateAccount(token);
        if (isActivated) {
            return ResponseEntity.ok("Account activated successfully. You can now log in.");
        } else {
            return ResponseEntity.badRequest().body("Invalid activation token.");
        }
    }

    @GetMapping("/{id}")
    public Optional<RegisteredUser> findRegistrovaniKorisnikById(@PathVariable("id") Integer id) {
        return registeredUserService.findById(id);
    }

    @PostMapping("/add")
    public RegisteredUser saveRegistrovaniKorisnik(@RequestBody RegisteredUser registeredUser) {
        return registeredUserService.saveRegistrovaniKorisnik(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> prijaviKorisnika(@RequestBody RegisteredUser korisnik) {
        RegisteredUser validCredentials = registeredUserService.proveriKorisnika(korisnik.getEmail(), korisnik.getPassword());
        if (validCredentials != null) {
            return ResponseEntity.ok("{\"message\": \"Uspesna prijava.\"}");
        } else {
            return ResponseEntity.status(401).body("Neuspešna prijava. Proverite email i lozinku.");
        }
    }

    @PutMapping
    public RegisteredUser updateRegistrovaniKorisnik(@RequestBody RegisteredUser employeeEntity) {
        return registeredUserService.updateRegistrovaniKorisnik(employeeEntity);
    }

    @DeleteMapping("/{id}")
    public void deleteRegistrovaniKorisnik(@PathVariable("id") Integer id) {
        registeredUserService.deleteRegistrovaniKorisnik(id);
    }

    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {
        List<String> emails = registeredUserService.getAllEmails();
        return ResponseEntity.ok(emails);
    }


    @GetMapping("/username")
    public ResponseEntity<List<String>> getAllUsernames() {

        List<String> usernames = registeredUserService.getAllUsernames();
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






}
