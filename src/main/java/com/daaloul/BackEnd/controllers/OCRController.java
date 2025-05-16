package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.services.OCRService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/ocr")
public class OCRController {

    private final OCRService ocrService;

    public OCRController(OCRService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/extract")
    public ResponseEntity<String> extractText(@RequestParam("file") MultipartFile file) {
        try {
            File tempFile = File.createTempFile("uploaded-", ".pdf");
            file.transferTo(tempFile);

            String extractedText = ocrService.extractText(tempFile);
            return ResponseEntity.ok(extractedText);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("File processing error");
        }
    }
}