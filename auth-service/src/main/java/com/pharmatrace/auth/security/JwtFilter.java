package com.pharmatrace.auth.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import java.util.Collections;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        try {

            // Get Authorization Header
            String authHeader =
                    request.getHeader("Authorization");

            // Check Bearer Token
            if (authHeader != null &&
                    authHeader.startsWith("Bearer ")) {

                // Remove "Bearer "
                String token =
                        authHeader.substring(7);

                // Validate Token
                if (jwtUtil.validateToken(token)) {

                    // Extract Data
                    String email =
                            jwtUtil.extractEmail(token);

                    String role =
                            jwtUtil.extractRole(token);

                    // Create Authentication Object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    email,
                                    null,
                                    Collections.singletonList(
                                            new SimpleGrantedAuthority(
                                                    "ROLE_" + role
                                            )
                                    )
                            );

                    // Set Authentication
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (Exception e) {

            System.out.println("JWT Error: " + e.getMessage());
        }

        // Continue Request
        filterChain.doFilter(request, response);
    }
}