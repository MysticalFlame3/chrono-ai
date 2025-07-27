package com.chronoai.backend.repository;

import com.chronoai.backend.model.Task;
import com.chronoai.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    // New method to find all tasks for a specific user
    List<Task> findByUser(User user);
}