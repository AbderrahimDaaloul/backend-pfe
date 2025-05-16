package com.daaloul.BackEnd.services;

import com.daaloul.BackEnd.models.Student;
import com.daaloul.BackEnd.repos.StudentRepo;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;


    @Autowired
     private PasswordEncoder passwordEncoder;

    @Autowired
    private StudentRepo studentRepository;
    @Async
    public void sendHiringCongratulationsEmail(Student student, String companyName) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        
        helper.setTo(student.getEmail());
        helper.setSubject("Congratulations! You've Been Selected for Hiring at " + companyName);
        
        // Create the Thymeleaf context
        Context context = new Context();
        context.setVariable("studentName", student.getName());
        context.setVariable("companyName", companyName);
        
        // Process the template
        String htmlContent = templateEngine.process("hiring-congratulations", context);
        helper.setText(htmlContent, true);
        
        mailSender.send(message);
    }


    @Async
    public void sendInternshipEmailWithCredentials(Student student, String companyName) throws MessagingException {
        // 1. Generate a random password
        String generatedPassword = generateSecurePassword();
    
        // 2. Hash and update the student's password (assuming Student has setPassword())
        String hashedPassword = passwordEncoder.encode(generatedPassword);
        student.setPassword(hashedPassword);
        studentRepository.save(student);
    
        // 3. Prepare email
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    
        helper.setTo(student.getEmail());
        helper.setSubject("Congratulations! Your Internship & Login Details at " + companyName);
    
        Context context = new Context();
        context.setVariable("studentName", student.getName());
        context.setVariable("companyName", companyName);
        context.setVariable("email", student.getEmail());
        context.setVariable("password", generatedPassword);
    
        String htmlContent = templateEngine.process("internship-credentials-congratulations", context);
        helper.setText(htmlContent, true);
    
        mailSender.send(message);
    }
    
    private String generateSecurePassword() {
        return UUID.randomUUID().toString().substring(0, 8); // You can use better generation logic if needed
    }
    
}