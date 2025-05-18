package com.developer.onlybuns.controller;


import com.developer.onlybuns.dto.request.JwtUtil;
import com.developer.onlybuns.dto.request.LoginDTO;
import com.developer.onlybuns.entity.User;
import com.developer.onlybuns.service.KorisnikService;
import com.developer.onlybuns.service.RateLimiterService;
import io.github.resilience4j.ratelimiter.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/auth")
public class AuthController {


    @Autowired
    private final RateLimiterService rateLimiterService;

    @Autowired
    private final KorisnikService korisnikService;

    public AuthController(RateLimiterService rateLimiterService, KorisnikService korisnikService) {
        this.rateLimiterService = rateLimiterService;
        this.korisnikService = korisnikService;
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam  String email,@RequestParam String password,
                                   @RequestParam String ipAddress) {
        if (ipAddress == null) {
            return ResponseEntity.status(401).body("{\"message\": \"No IP address found.\"}");
        }
        if (rateLimiterService.isRateLimited(ipAddress)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("{\"message\": \"Too many login attempts. Please try again later.\"}");
        }

        User validCredentials = korisnikService.findByEmailAndPassword(email, password);
        if (validCredentials != null) {
            User user = korisnikService.findByEmail(email);

            if (user.isVerified()) {
                String username = user.getUsername();
                String uloga = user.getUloga().toString();

                JwtUtil jwtUtil = new JwtUtil();
                String token = jwtUtil.generateToken(email, username, uloga);

                Map<String, String> response = new HashMap<>();
                response.put("token", token);

                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(401).body("{\"message\": \"Please activate your account.\"}");
            }

        } else {

            return ResponseEntity.status(401).body("{\"message\": \"Email or password is incorrect.\"}");
        }
    }

    @PostMapping("/decodeJwt")
    public ResponseEntity<?> decodeJwt(@RequestBody String token) {
        if (!JwtUtil.validateToken(token)) {
            return ResponseEntity.status(400).body("Invalid JWT token");
        }

        try {
            // Extract korisnik (email) from the JWT token
            String email = JwtUtil.getEmailFromToken(token);

            String username = JwtUtil.getUsernameFromToken(token);

            String role = JwtUtil.getRoleFromToken(token);

            Map<String, String> response = new HashMap<>();
            response.put("Email", email);
            response.put("Username", username);
            response.put("Role", role);

            // Return the extracted data in the response
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Any other exception
            return ResponseEntity.status(400).body("Error decoding JWT token");
        }
    }
    public ResponseEntity<?> rateLimitExceededFallback(
            LoginDTO loginDTO,
            String ipAddress,
            Throwable throwable) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Too many login attempts");
        errorResponse.put("message", "Too many login attempts. Please try again later.");
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
    }

    @GetMapping("/rate-limiters")
    public ResponseEntity<Map<String, String>> listRateLimiters() {
        Map<String, RateLimiter> limiters = rateLimiterService.listAllRateLimiters();
        Map<String, String> result = new HashMap<>();
        limiters.forEach((ip, limiter) -> {
            result.put(ip, limiter.getMetrics().toString());
        });
        return ResponseEntity.ok(result);
    }
}





