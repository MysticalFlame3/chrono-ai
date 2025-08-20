package com.chronoai.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class GeminiService {

    private static final Logger logger = LoggerFactory.getLogger(GeminiService.class);

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    /**
     * A general method to generate content with specific parameters.
     * @param prompt The text prompt to send to the model.
     * @param temperature Controls randomness (0.0 for deterministic, 1.0 for creative).
     * @param maxTokens The maximum number of tokens in the response.
     * @return The text response from the AI.
     */
    public String generateContent(String prompt, double temperature, int maxTokens) {
        try {
            // Build the complex JSON body required by the Gemini API
            String requestBody = String.format(
                    "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}], \"generationConfig\": {\"temperature\": %f, \"maxOutputTokens\": %d}}",
                    prompt.replace("\"", "\\\""), // Escape quotes in the prompt
                    temperature,
                    maxTokens
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_API_URL + apiKey))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                logger.error("Gemini API request failed with status code {}: {}", response.statusCode(), response.body());
                throw new RuntimeException("Failed to communicate with Gemini API: " + response.body());
            }

            return extractTextFromResponse(response.body());

        } catch (IOException | InterruptedException e) {
            logger.error("Failed to generate content with Gemini API", e);
            throw new RuntimeException("Failed to generate content", e);
        }
    }

    /**
     * A simpler method for chat-like interactions with default parameters.
     * @param message The user's message.
     * @return The AI's response.
     */
    public String sendMessage(String message) {
        // Uses the general method with default settings
        return generateContent(message, 0.7, 1000);
    }

    /**
     * A helper method to parse the text from the complex JSON response.
     * @param responseBody The full JSON response from the API.
     * @return The extracted text content.
     */
    private String extractTextFromResponse(String responseBody) {
        try {
            int textIndex = responseBody.indexOf("\"text\": \"");
            if (textIndex == -1) {
                // Handle potential errors or different response structures
                if (responseBody.contains("error")) {
                    logger.error("Gemini API returned an error: {}", responseBody);
                    return "Error: " + responseBody;
                }
                throw new RuntimeException("Could not find 'text' in Gemini response: " + responseBody);
            }
            String sub = responseBody.substring(textIndex + 9);
            int endIndex = sub.indexOf("\"");
            return sub.substring(0, endIndex).replace("\\n", " ").trim();
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Gemini response: " + responseBody, e);
        }
    }
}
