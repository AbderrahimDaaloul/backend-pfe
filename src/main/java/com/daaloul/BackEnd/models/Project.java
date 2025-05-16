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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // Use TABLE_PER_CLASS strategy
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Uses UUID generation in Spring Boot 3+
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true) // Ensures name is required and unique
    private String name;

    @Column(columnDefinition = "TEXT") // Allows long descriptions
    private String description;


}
