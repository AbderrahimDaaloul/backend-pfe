package com.daaloul.BackEnd.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questions")
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Uses UUID natively in Spring Boot 3+
    @Column(columnDefinition = "UUID", updatable = false, nullable = false) // Ensures the database uses UUID type
    private UUID id;
    @Column(length = 2000) // Increase length for the question field
    private String question;

    @Column(name = "llm_answer", length = 2000) // Ensures proper column naming in DB
    private String llmAnswer;

    @Column(name = "student_answer", length = 2000) // Ensures proper column naming in DB
    private String studentAnswer;

    private double score;

    @ManyToOne
    @JoinColumn(name = "test_id") // FK in Question table
    private Test test;


}
