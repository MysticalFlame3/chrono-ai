package com.chronoai.backend.controller;

import com.chronoai.backend.dto.ParseRequest;
import com.chronoai.backend.model.Task;
import com.chronoai.backend.model.User;
import com.chronoai.backend.repository.TaskRepository;
import com.chronoai.backend.repository.UserRepository;
import com.chronoai.backend.service.AIService;
import com.chronoai.backend.service.SchedulerService;
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
    private final SchedulerService schedulerService;
    private final AIService aiService;

    @Autowired
    public TaskController(TaskRepository taskRepository, UserRepository userRepository, SchedulerService schedulerService, AIService aiService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.schedulerService = schedulerService;
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("[TaskController] Authentication: " + authentication);
        System.out.println("[TaskController] Principal: " + authentication.getPrincipal());
        System.out.println("[TaskController] Authorities: " + authentication.getAuthorities());
        String username = authentication.getName();
        System.out.println("[TaskController] Username from authentication: " + username);
        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (user == null) {
            System.err.println("User not found for username: " + username);
            return ResponseEntity.status(401).body("User not found: " + username);
        }
        // Validate notificationType
        if (task.getNotificationType() == null) {
            return ResponseEntity.badRequest().body("Invalid or missing notificationType. Must be 'EMAIL' or 'WEBHOOK'.");
        }
        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        schedulerService.scheduleJob(savedTask); // Schedule the job
        return ResponseEntity.ok(savedTask);
    }

    @GetMapping
    public ResponseEntity<?> getUserTasks() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        List<Task> userTasks = taskRepository.findByUser(user);
        return ResponseEntity.ok(userTasks);
    }

    @PostMapping("/parse")
    public ResponseEntity<String> parseTaskString(@RequestBody ParseRequest parseRequest) throws IOException, InterruptedException {
        String cronExpression = aiService.parseNaturalLanguageToCron(parseRequest.getQuery());
        return ResponseEntity.ok(cronExpression);
    }
}