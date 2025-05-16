package com.daaloul.BackEnd.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GeminiService {

    @Value("${gemini.api.key}") // Load API key from application.properties
    private String apiKey;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=";

    public String generateText(String prompt) throws IOException {
        String url = API_URL + apiKey;

        // Use ObjectMapper to construct the JSON payload safely
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode requestJson = mapper.createObjectNode();
        ArrayNode contentsArray = requestJson.putArray("contents");
        ObjectNode content = contentsArray.addObject();
        ArrayNode partsArray = content.putArray("parts");
        ObjectNode part = partsArray.addObject();
        part.put("text", prompt); // Safely add the prompt text

        // Convert the JSON object to a string
        String jsonInput = mapper.writeValueAsString(requestJson);

        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpPost request = new HttpPost(url);
            request.setHeader("Content-Type", "application/json");
            request.setEntity(new StringEntity(jsonInput));

            try (CloseableHttpResponse response = client.execute(request)) {
                JsonNode jsonResponse = mapper.readTree(response.getEntity().getContent());

                // Log the full response for debugging
                System.out.println("API Response: " + jsonResponse.toPrettyString());

                // Handle API errors
                if (jsonResponse.has("error")) {
                    return "Error: " + jsonResponse.path("error").path("message").asText();
                }

                // Check if the response contains candidates
                if (jsonResponse.has("candidates") && jsonResponse.path("candidates").isArray() && jsonResponse.path("candidates").size() > 0) {
                    return jsonResponse.path("candidates").get(0).path("content").path("parts").get(0).path("text").asText();
                } else {
                    return "Error: No valid response from the API.";
                }
            }
        }
    }
}