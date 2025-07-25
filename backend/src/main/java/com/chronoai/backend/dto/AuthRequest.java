package com.chronoai.backend.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;

    // Manually added getter for username
    public String getUsername() {
        return username;
    }

    // Manually added getter for password
    public String getPassword() {
        return password;
    }

    // Manually added setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Manually added setter for password
    public void setPassword(String password) {
        this.password = password;
    }
}