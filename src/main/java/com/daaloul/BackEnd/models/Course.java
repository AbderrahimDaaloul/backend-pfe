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
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Uses UUID natively in Spring Boot 3+
    private UUID id;

    @Column(nullable = false) // Ensures name is required
    private String name;

    @Column(length = 1000) // Allows for longer descriptions
    private String description;

    private String certificationLink;


    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;


}
