package com.daaloul.BackEnd.models;

import com.daaloul.BackEnd.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Uses UUID generation in Spring Boot 3+
    @Column(columnDefinition = "UUID", updatable = false, nullable = false) // Ensures the database uses UUID type
    private UUID id;

    @Column(nullable = false) // Ensures name cannot be null
    private String name;

    private String description;

    @Temporal(TemporalType.TIMESTAMP) // Ensures proper timestamp storage
    private Date deadline;

    @Enumerated(EnumType.STRING) // Stores enum as a readable string in the database
    private Status status;

    private String solutionPath;

    private double score;

    @ManyToOne
    @JoinColumn(name = "student_id")  // foreign key in Task table
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "intern_room_id", nullable = false)
    private InternRoom internRoom;

}
