package com.daaloul.BackEnd.models;

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
@Entity  // Marks this class as a JPA entity
@Table(name = "internships") // Defines the database table name
public class Internship {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Uses UUID generation in Spring Boot 3+
    @Column(columnDefinition = "UUID", updatable = false, nullable = false) // Ensures the database uses UUID type
    private UUID id;

    private String company;

    @Temporal(TemporalType.DATE) // Ensures only the date is stored (without time)
    private Date startingDate;

    @Temporal(TemporalType.DATE) // Ensures only the date is stored (without time)
    private Date endingDate;

    @Column(columnDefinition = "TEXT") // Allows storing long descriptions
    private String description;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false) // FK in Internship table
    private Student student;


}
