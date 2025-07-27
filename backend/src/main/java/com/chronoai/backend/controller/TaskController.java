package com.chronoai.backend.controller;

import com.chronoai.backend.dto.ParseRequest;
import com.chronoai.backend.model.Task;
import com.chronoai.backend.model.User;
import com.chronoai.backend.repository.TaskRepository;
import com.chronoai.backend.repository.UserRepository;
import com.chronoai.backend.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AIService aiService;

    @Autowired
    public TaskController(TaskRepository taskRepository, UserRepository userRepository, AIService aiService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("User not authenticated");
            }
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            // Associate the task with the authenticated user
            task.setUser(user);
            Task savedTask = taskRepository.save(task);
            return ResponseEntity.ok(savedTask);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating task: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getUserTasks() {
        try {
            // Get the authenticated user
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(401).body("User not authenticated");
            }
            
            String username = authentication.getName();
            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found: " + username));
            
            // Get tasks for the authenticated user
            List<Task> userTasks = taskRepository.findByUser(user);
            return ResponseEntity.ok(userTasks);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching tasks: " + e.getMessage());
        }
    }

    @PostMapping("/parse")
    // The change is here: added InterruptedException
    public ResponseEntity<String> parseTaskString(@RequestBody ParseRequest parseRequest) throws IOException, InterruptedException {
        String cronExpression = aiService.parseNaturalLanguageToCron(parseRequest.getQuery());
        return ResponseEntity.ok(cronExpression);
    }
}