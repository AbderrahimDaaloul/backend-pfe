package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.DTOs.StudentRegistrationByHimSelfDTO;
import com.daaloul.BackEnd.DTOs.StudentRegistrationDTO;
import com.daaloul.BackEnd.DTOs.StudentRegistrationResponse;
import com.daaloul.BackEnd.models.Student;
import com.daaloul.BackEnd.models.User;
import com.daaloul.BackEnd.services.StudentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;


    // Register a student
    @Transactional
    @PostMapping(value = "/register/student/by/himself", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerStudent( // Changed return type from ResponseEntity<String> to ResponseEntity<?>
            @RequestPart("studentData") String studentDataJson,
            @RequestPart("cv") MultipartFile cv,
            @RequestPart("coverLetter") MultipartFile coverLetter) throws IOException {

        // Parse the JSON string to a StudentRegistrationDTO object
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For LocalDate support
        StudentRegistrationByHimSelfDTO studentDto;
        try {
            studentDto = objectMapper.readValue(studentDataJson, StudentRegistrationByHimSelfDTO.class);
        } catch (JsonProcessingException e) {
            // Return a JSON error object
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "Invalid JSON data: " + e.getMessage()));
        }

        // Call the registerUser method with the DTO and files
        User registeredUser = studentService.registerStudentByHimself(studentDto, cv, coverLetter);

        if (registeredUser != null) {
            // Return the registeredUser's ID in the specified JSON format
            return ResponseEntity.ok(java.util.Map.of("id", registeredUser.getId()));
        } else {
            // Return a JSON error object
            return ResponseEntity.badRequest().body(java.util.Map.of("error", "Failed to register student"));
        }
    }


}

