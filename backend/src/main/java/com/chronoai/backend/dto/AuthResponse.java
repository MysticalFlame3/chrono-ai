package com.chronoai.backend.dto;

import lombok.Data;

@Data
public class AuthResponse {
    private String jwt;

    // Manually added constructor
    public AuthResponse(String jwt) {
        this.jwt = jwt;
    }
}