package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.DTOs.SaveTestRequest;
import com.daaloul.BackEnd.models.Question;
import com.daaloul.BackEnd.models.Test;
import com.daaloul.BackEnd.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class TestController {

    @Autowired
    private TestService testService;


    @PostMapping("/save/test")
    public ResponseEntity<Test> saveTest(@RequestBody SaveTestRequest request) {
        Test savedTest = testService.saveTest(request.getStudentId(), request.getQuestions());
        return ResponseEntity.ok(savedTest);
    }


    @GetMapping("/{testId}")
    public ResponseEntity<Test> getTestById(@PathVariable UUID testId) {
        Test test = testService.getTestById(testId);
        return ResponseEntity.ok(test);
    }
}