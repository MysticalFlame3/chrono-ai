package com.chronoai.backend.service;

import com.chronoai.backend.config.GeminiConfig;
import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.ChatSession;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@Service
public class GeminiService {
    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);
    private final GeminiConfig geminiConfig;
    private GenerativeModel model;
    private ChatSession chat;

    @Autowired
    public GeminiService(GeminiConfig geminiConfig) {
        this.geminiConfig = geminiConfig;
        initializeGemini();
    }

    private void initializeGemini() {
        try {
            VertexAI vertexAI = new VertexAI(geminiConfig.getKey());
            model = new GenerativeModel("gemini-pro", vertexAI);
            chat = model.startChat();
        } catch (Exception e) {
            logger.error("Failed to initialize Gemini API", e);
            throw new RuntimeException("Failed to initialize Gemini API", e);
        }
    }

  
    public String sendMessage(String message) {
        try {
            ResponseHandler response = chat.sendMessage(message);
            return response.getText();
        } catch (IOException e) {
            logger.error("Failed to send message to Gemini API", e);
            throw new RuntimeException("Failed to communicate with Gemini API", e);
        }
    }

    
    public String generateContent(String prompt, double temperature, int maxTokens) {
        try {
            return model.generateContent()
                    .setTemperature(temperature)
                    .setMaxTokens(maxTokens)
                    .setPrompt(prompt)
                    .execute()
                    .getText();
        } catch (IOException e) {
            logger.error("Failed to generate content with Gemini API", e);
            throw new RuntimeException("Failed to generate content", e);
        }
    }

   
    public String processTask(String taskDescription, String context) {
        try {
            StringBuilder prompt = new StringBuilder();
            prompt.append("Task: ").append(taskDescription).append("\n");
            prompt.append("Context: ").append(context).append("\n");
            prompt.append("Please process this task considering the given context.");

            return generateContent(prompt.toString(), 0.7, 1000);
        } catch (Exception e) {
            logger.error("Failed to process task with Gemini API", e);
            throw new RuntimeException("Failed to process task", e);
        }
    }

    
    public List<String> streamResponse(String prompt) {
        List<String> responses = new ArrayList<>();
        try {
            model.generateContentStream(prompt)
                .forEach(response -> responses.add(response.getText()));
            return responses;
        } catch (IOException e) {
            logger.error("Failed to stream response from Gemini API", e);
            throw new RuntimeException("Failed to stream response", e);
        }
    }

   
    public void resetChat() {
        chat = model.startChat();
    }
}
