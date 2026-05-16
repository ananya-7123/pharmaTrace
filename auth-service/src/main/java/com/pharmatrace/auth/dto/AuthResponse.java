package com.pharmatrace.auth.dto;

import com.pharmatrace.auth.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class AuthResponse {

    private Long userId;

    private String email;

    private String fullName;

    private Role role;

    private String token;

    private LocalDateTime createdAt;

    private String message;

}