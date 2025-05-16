package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.models.Student;
import com.daaloul.BackEnd.services.EmailService;
import com.daaloul.BackEnd.services.StudentService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class HiringNotificationController {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private StudentService studentService;
    
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/notify-student-hiring")
    public ResponseEntity<?> notifyStudentHiring(
            @RequestParam("studentId") UUID studentId,
            @RequestParam("companyName") String companyName) {
        
        try {
            Optional<Student> studentOpt = studentService.findStudentByID(studentId);
            
            if (studentOpt.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Student not found"));
            }
            
            Student student = studentOpt.get();
            emailService.sendHiringCongratulationsEmail(student, companyName);
            
            return ResponseEntity.ok(Map.of(
                "message", "Hiring notification email sent successfully",
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