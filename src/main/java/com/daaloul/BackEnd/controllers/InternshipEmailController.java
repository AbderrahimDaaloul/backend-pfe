
package com.daaloul.BackEnd.controllers;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.daaloul.BackEnd.models.Student;
import com.daaloul.BackEnd.services.EmailService;
import com.daaloul.BackEnd.services.StudentService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/internship")
public class InternshipEmailController {

    private final EmailService emailService;

    @Autowired
    public InternshipEmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @Autowired
    private StudentService studentService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/notify-student-internship")
    public ResponseEntity<?> notifyStudentInternship(
            @RequestParam("studentId") UUID studentId,
            @RequestParam("companyName") String companyName) {
        
        try {
            Optional<Student> studentOpt = studentService.findStudentByID(studentId);
            
            if (studentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Student not found"));
            }
            
            Student student = studentOpt.get();
            // Use the internship email method instead of hiring email
            emailService.sendInternshipEmailWithCredentials(student, companyName);
            
            return ResponseEntity.ok(Map.of(
                "message", "Internship notification with credentials sent successfully",
                "studentId", studentId,
                "studentEmail", student.getEmail(),
                "companyName", companyName
            ));
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body(Map.of(
                "error", "Failed to send email: " + e.getMessage()
            ));
        }
    }
}