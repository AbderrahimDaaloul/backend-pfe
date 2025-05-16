package com.daaloul.BackEnd.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity  // Marks this class as a JPA entity
@Table(name = "tests") // Optional: Defines table name in the database
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Uses UUID natively in Spring Boot 3+
    @Column(columnDefinition = "UUID", updatable = false, nullable = false) // Ensures the database uses UUID type
    private UUID id;

    private double result;

    @OneToOne
    @JoinColumn(name = "student_id") // FK in Test table
    private Student student;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();


}
