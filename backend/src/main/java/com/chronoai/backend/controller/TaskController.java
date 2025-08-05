package com.chronoai.backend.controller;

import com.chronoai.backend.dto.ParseRequest;
import com.chronoai.backend.model.ExecutionLog;
import com.chronoai.backend.model.Task;
import com.chronoai.backend.model.User;
import com.chronoai.backend.repository.ExecutionLogRepository;
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
    private final ExecutionLogRepository executionLogRepository;

    @Autowired
    public TaskController(TaskRepository taskRepository, UserRepository userRepository,
                          SchedulerService schedulerService, AIService aiService,
                          ExecutionLogRepository executionLogRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.schedulerService = schedulerService;
        this.aiService = aiService;
        this.executionLogRepository = executionLogRepository;
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody Task task) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        task.setUser(user);
        Task savedTask = taskRepository.save(task);
        schedulerService.scheduleJob(savedTask);

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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        schedulerService.deleteJob(id);
        taskRepository.deleteById(id);
        return ResponseEntity.ok("Task deleted successfully");
    }
    
    @GetMapping("/{taskId}/history")
    public ResponseEntity<?> getTaskHistory(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!task.getUser().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(403).body("Access denied");
        }

        List<ExecutionLog> history = executionLogRepository.findByTask(task);
        return ResponseEntity.ok(history);
    }

    @PostMapping("/parse")
    public ResponseEntity<String> parseTaskString(@RequestBody ParseRequest parseRequest) throws IOException, InterruptedException {
        String cronExpression = aiService.parseNaturalLanguageToCron(parseRequest.getQuery());
        return ResponseEntity.ok(cronExpression);
    }
}