package com.pharmatrace.auth.security;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public JwtUtil() {
    }

    public String generateToken(String username) {
        // TODO: Implement JWT token generation
        return null;
    }

    public String extractUsername(String token) {
        // TODO: Implement username extraction from token
        return null;
    }

    public boolean validateToken(String token) {
        // TODO: Implement token validation
        return false;
    }
}
