package com.pharmatrace.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    // Generate JWT Token
    public String generateToken(String email, String role) {

        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(
                        Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    // Extract Email
    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract Role
    public String extractRole(String token) {
        return extractClaim(token, claims -> claims.get("role", String.class));
    }

    // Extract Expiry
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Generic Claim Extractor
    public <T> T extractClaim(String token, Function<Claims, T> resolver) {

        Claims claims = extractAllClaims(token);

        return resolver.apply(claims);
    }

    // Extract All Claims
    private Claims extractAllClaims(String token) {

    return Jwts.parser()
            .verifyWith(
                    Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8))
            )
            .build()
            .parseSignedClaims(token)
            .getPayload();
}

    // Check Expiry
    private Boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }

    // Validate Token
    public Boolean validateToken(String token, String email) {

        String extractedEmail = extractEmail(token);

        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    public Boolean validateToken(String token) {

    try {

        return !isTokenExpired(token);

    } catch (Exception e) {

        return false;
    }
}
}