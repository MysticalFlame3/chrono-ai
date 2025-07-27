package com.chronoai.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public String parseNaturalLanguageToCron(String query) throws IOException, InterruptedException {
        // For testing purposes, if API key is not properly set, return a default cron expression
        if (apiKey == null || apiKey.equals("test-key-for-demo") || apiKey.trim().isEmpty()) {
            return getDefaultCronExpression(query);
        }

        String prompt = "Convert the following user request into a standard 5-field CRON expression. " +
                "The fields are: minute, hour, day of month, month, day of week. " +
                "Only return the CRON expression and nothing else. " +
                "Example Request: 'every day at 5am'. Response: '0 5 * * *'. " +
                "Example Request: 'every 10 minutes'. Response: '*/10 * * * *'. " +
                "Example Request: 'at 11:30 PM on the 15th of every month'. Response: '30 23 15 * *'. " +
                "User Request: '" + query + "'";

        // Create the JSON request body
        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

        // Build the HTTP request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(GEMINI_API_URL + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Send the request and get the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Extract the text from the JSON response
        String cronExpression = extractTextFromResponse(response.body());

        if (cronExpression.matches("^[\\*\\/\\d\\s,-]+$") && cronExpression.split(" ").length >= 5) {
            return cronExpression;
        } else {
            throw new IllegalArgumentException("AI failed to generate a valid CRON expression. Response: " + cronExpression);
        }
    }

    // Simple fallback for testing when API key is not configured
    private String getDefaultCronExpression(String query) {
        query = query.toLowerCase();
        if (query.contains("every day") || query.contains("daily")) {
            return "0 9 * * *"; // Daily at 9 AM
        } else if (query.contains("every hour") || query.contains("hourly")) {
            return "0 * * * *"; // Every hour at minute 0
        } else if (query.contains("every minute")) {
            return "* * * * *"; // Every minute
        } else if (query.contains("weekly") || query.contains("every week")) {
            return "0 9 * * 1"; // Every Monday at 9 AM
        } else if (query.contains("5am") || query.contains("5 am")) {
            return "0 5 * * *"; // Daily at 5 AM
        } else {
            return "0 9 * * *"; // Default: Daily at 9 AM
        }
    }

    // A simple helper method to parse the JSON response without extra libraries
    private String extractTextFromResponse(String responseBody) {
        try {
            int textIndex = responseBody.indexOf("\"text\": \"");
            if (textIndex == -1) {
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