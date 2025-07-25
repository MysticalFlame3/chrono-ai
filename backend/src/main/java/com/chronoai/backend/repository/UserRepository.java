package com.chronoai.backend.repository;

import com.chronoai.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // We need a custom method to find a user by their username.
    // This will be used during the login process.
    Optional<User> findByUsername(String username);
}