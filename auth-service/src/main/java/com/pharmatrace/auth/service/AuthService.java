package com.pharmatrace.auth.service;

import com.pharmatrace.auth.dto.AuthResponse;
import com.pharmatrace.auth.dto.LoginRequest;
import com.pharmatrace.auth.dto.RegisterRequest;

import com.pharmatrace.auth.entity.Role;
import com.pharmatrace.auth.entity.User;

import com.pharmatrace.auth.repository.UserRepository;
import com.pharmatrace.auth.security.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register User
    public AuthResponse register(RegisterRequest request) {

        // Check Role
        Role role;

        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (Exception e) {

            return new AuthResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Invalid role"
            );
        }

        // Check Existing Email
        if (userRepository.existsByEmail(request.getEmail())) {

            return new AuthResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Email already exists"
            );
        }

        // Create User
        User user = new User();

        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setFullName(request.getFullName());

        user.setRole(role);

        user.setActive(true);

        user.setCreatedAt(LocalDateTime.now());

        user.setUpdatedAt(LocalDateTime.now());

        // Save User
        User savedUser = userRepository.save(user);

        // Generate JWT
        String token = jwtUtil.generateToken(
                savedUser.getEmail(),
                savedUser.getRole().toString()
        );

        // Return Response
        return new AuthResponse(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFullName(),
                savedUser.getRole(),
                token,
                savedUser.getCreatedAt(),
                "User registered successfully"
        );
    }

    // Login User
    public AuthResponse login(LoginRequest request) {

        Optional<User> userOptional =
                userRepository.findByEmail(request.getEmail());

        // User Not Found
        if (userOptional.isEmpty()) {

            return new AuthResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Invalid email or password"
            );
        }

        User user = userOptional.get();

        // Check Password
        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            return new AuthResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Invalid email or password"
            );
        }

        // Check Active
        if (!user.getActive()) {

            return new AuthResponse(
                    null,
                    null,
                    null,
                    null,
                    null,
                    null,
                    "Account inactive"
            );
        }

        // Generate JWT
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().toString()
        );

        // Return Response
        return new AuthResponse(
                user.getId(),
                user.getEmail(),
                user.getFullName(),
                user.getRole(),
                token,
                user.getCreatedAt(),
                "Login successful"
        );
    }

    // Find User By ID
    public Optional<User> getUserById(Long id) {

        return userRepository.findById(id);
    }

    // Find User By Email
    public Optional<User> getUserByEmail(String email) {

        return userRepository.findByEmail(email);
    }
}