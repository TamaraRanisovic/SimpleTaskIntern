package com.developer.onlybuns.dto.request;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Claims;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET_KEY = "your_secret_key";
    private static final long EXPIRATION_TIME = 600000; // 10 minutes in milliseconds

    // Generate JWT token
    public String generateToken(String email, String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Validate JWT token
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Extract korisnik data from JWT token
    public static String getKorisnikFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public static String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JwtUtil.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("sub", String.class);
        } catch (Exception e) {
            return "Subject not found";
        }
    }

    public static String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JwtUtil.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("username", String.class);
        } catch (Exception e) {
            return "Username not found";
        }
    }

    public static String getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(JwtUtil.SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            return claims.get("role", String.class); // Extract 'role' claim from the token
        } catch (Exception e) {
            return "Role not found"; // Handle case where 'role' claim is missing
        }
    }


}
