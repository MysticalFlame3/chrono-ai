package com.chronoai.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore
    private String password;

    // Manually added setter for username
    public void setUsername(String username) {
        this.username = username;
    }

    // Manually added setter for password
    public void setPassword(String password) {
        this.password = password;
    }

    // Manually added getter for username
    public String getUsername() {
        return this.username;
    }

    // Manually added getter for password
    public String getPassword() {
        return this.password;
    }
}