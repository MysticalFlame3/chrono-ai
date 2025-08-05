package com.chronoai.backend.controller;

import com.chronoai.backend.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
public class GeminiController {
    private final GeminiService geminiService;

    @Autowired
    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/chat")
    public ResponseEntity<String> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        return ResponseEntity.ok(geminiService.sendMessage(message));
    }

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody Map<String, Object> request) {
        String prompt = (String) request.get("prompt");
        double temperature = request.containsKey("temperature") ? 
            ((Number) request.get("temperature")).doubleValue() : 0.7;
        int maxTokens = request.containsKey("maxTokens") ? 
            ((Number) request.get("maxTokens")).intValue() : 1000;
        
        return ResponseEntity.ok(geminiService.generateContent(prompt, temperature, maxTokens));
    }

    @PostMapping("/process-task")
    public ResponseEntity<String> processTask(@RequestBody Map<String, String> request) {
        String taskDescription = request.get("task");
        String context = request.get("context");
        return ResponseEntity.ok(geminiService.processTask(taskDescription, context));
    }

    @PostMapping("/stream")
    public ResponseEntity<List<String>> streamResponse(@RequestBody Map<String, String> request) {
        String prompt = request.get("prompt");
        return ResponseEntity.ok(geminiService.streamResponse(prompt));
    }

    @PostMapping("/reset")
    public ResponseEntity<Void> resetChat() {
        geminiService.resetChat();
        return ResponseEntity.ok().build();
    }
}
