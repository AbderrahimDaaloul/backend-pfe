package com.daaloul.BackEnd.DTOs;

import com.daaloul.BackEnd.models.Student;


public class StudentRegistrationResponse {
    private Student student;
    private String extractedCvText;

    // Getters and setters
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public String getExtractedCvText() {
        return extractedCvText;
    }

    public void setExtractedCvText(String extractedCvText) {
        this.extractedCvText = extractedCvText;
    }
}