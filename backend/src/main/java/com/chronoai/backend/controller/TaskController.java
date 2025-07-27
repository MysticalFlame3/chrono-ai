package com.chronoai.backend.controller;

import com.chronoai.backend.dto.ParseRequest;
import com.chronoai.backend.model.Task;
import com.chronoai.backend.repository.TaskRepository;
import com.chronoai.backend.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskRepository taskRepository;
    private final AIService aiService;

    @Autowired
    public TaskController(TaskRepository taskRepository, AIService aiService) {
        this.taskRepository = taskRepository;
        this.aiService = aiService;
    }

    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask);
    }

    @PostMapping("/parse")
    // The change is here: added InterruptedException
    public ResponseEntity<String> parseTaskString(@RequestBody ParseRequest parseRequest) throws IOException, InterruptedException {
        String cronExpression = aiService.parseNaturalLanguageToCron(parseRequest.getQuery());
        return ResponseEntity.ok(cronExpression);
    }
}