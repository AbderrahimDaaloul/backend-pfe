package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.services.OllamaService;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ollama")
public class OllamaController {

    @Autowired
    private OllamaService ollamaService;

    @PostMapping("/generate")
    public JsonNode generate(@RequestBody Map<String, String> body) {
        String prompt = body.get("prompt");
        return ollamaService.generateResponse(prompt);
    }
}
