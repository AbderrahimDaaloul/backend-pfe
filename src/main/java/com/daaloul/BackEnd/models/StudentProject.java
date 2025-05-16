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
public class StudentProject extends Project {

    @Column(nullable = false, unique = true)
    private String githubLink;

    @ManyToOne
    @JoinColumn(name = "student_id") // FK in StudentProject table
    private Student student;


}
