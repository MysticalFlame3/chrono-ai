package com.chronoai.backend.controller;

import com.chronoai.backend.dto.AuthRequest;
import com.chronoai.backend.dto.AuthResponse;
import com.chronoai.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder; 
    @Autowired
    public AuthController(AuthService authService, PasswordEncoder passwordEncoder) { 
        this.authService = authService;
        this.passwordEncoder = passwordEncoder; 
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        authService.registerUser(authRequest);
        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authRequest) {
        final String jwt = authService.loginUser(authRequest);
        return ResponseEntity.ok(new AuthResponse(jwt));


    }

    @GetMapping("/hash/{password}")
    public String getPasswordHash(@PathVariable String password) {
        return "Hash for '" + password + "': " + passwordEncoder.encode(password);
    }
}
