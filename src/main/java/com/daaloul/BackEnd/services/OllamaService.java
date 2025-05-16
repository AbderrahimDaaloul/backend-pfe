package com.daaloul.BackEnd.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class OllamaService {

    private static final String OLLAMA_API_URL = "http://localhost:11434";
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public OllamaService(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClient = webClientBuilder.baseUrl(OLLAMA_API_URL).build();
        this.objectMapper = objectMapper;
    }

    public JsonNode generateResponse(String prompt) {
        Mono<String> responseMono = webClient.post()
                .uri("/api/generate")
                .bodyValue(Map.of("model", "llama3.2:latest", "prompt", prompt, "stream", false))
                .retrieve()
                .bodyToMono(String.class);

        String responseString = responseMono.block();

        try {
            return objectMapper.readTree(responseString);
        } catch (Exception e) {
            e.printStackTrace();
            return objectMapper.createObjectNode().put("error", "Error parsing response from Ollama");
        }
    }
}
