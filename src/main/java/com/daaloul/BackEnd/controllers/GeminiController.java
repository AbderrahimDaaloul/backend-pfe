package com.daaloul.BackEnd.controllers;


import com.daaloul.BackEnd.services.GeminiService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/gemini")
public class GeminiController {

    private final GeminiService geminiService;

    public GeminiController(GeminiService geminiService) {
        this.geminiService = geminiService;
    }

    @PostMapping("/generate")
    public String generateText(@RequestBody String prompt) {
        try {
            return geminiService.generateText(prompt);
        } catch (IOException e) {
            return "Error: " + e.getMessage();
        }
    }
}