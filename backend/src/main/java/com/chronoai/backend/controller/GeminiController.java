package com.chronoai.backend.controller;

import com.chronoai.backend.service.GeminiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        // Use default values if the keys are not present
        double temperature = request.containsKey("temperature") ?
                ((Number) request.get("temperature")).doubleValue() : 0.7;
        int maxTokens = request.containsKey("maxTokens") ?
                ((Number) request.get("maxTokens")).intValue() : 1000;

        return ResponseEntity.ok(geminiService.generateContent(prompt, temperature, maxTokens));
    }
}
