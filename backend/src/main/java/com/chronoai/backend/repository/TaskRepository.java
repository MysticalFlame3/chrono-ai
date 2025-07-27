package com.chronoai.backend.repository;

import com.chronoai.backend.model.Task;
import com.chronoai.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // By extending JpaRepository, we get a lot of powerful database
    // methods for free, like save(), findById(), findAll(), delete(), etc.
    // We can add custom query methods here later if we need them.

    List<Task> findByUser(User user);
}