package com.chronoai.backend.controller;

import com.chronoai.backend.model.Task;
import com.chronoai.backend.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks") // All endpoints in this class will start with /api/tasks
public class TaskController {

    private final TaskRepository taskRepository;

    // Using constructor injection is a best practice for dependencies
    @Autowired
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @PostMapping // Handles HTTP POST requests to /api/tasks
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        // @RequestBody tells Spring to convert the incoming JSON into a Task object
        Task savedTask = taskRepository.save(task);
        return ResponseEntity.ok(savedTask); // Returns the saved task with its new ID
    }
}