package com.pharmatrace.auth.controller;

import com.pharmatrace.auth.dto.AuthResponse;
import com.pharmatrace.auth.dto.LoginRequest;
import com.pharmatrace.auth.dto.RegisterRequest;

import com.pharmatrace.auth.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")

@CrossOrigin(origins = "*")

public class AuthController {

    @Autowired
    private AuthService authService;

    // Register API
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        AuthResponse response =
                authService.register(request);

        if (response.getToken() != null) {

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);
        }

        return ResponseEntity
                .badRequest()
                .body(response);
    }

    // Login API
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        AuthResponse response =
                authService.login(request);

        if (response.getToken() != null) {

            return ResponseEntity.ok(response);
        }

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(response);
    }

    // Health Check
    @GetMapping("/health")
    public String health() {

        return "Auth Service Running";
    }
}