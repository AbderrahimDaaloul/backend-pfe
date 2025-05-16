package com.daaloul.BackEnd.models;

import com.daaloul.BackEnd.enums.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
public class Student extends User {

    private LocalDate birthDate;
    private String linkedIn;
    private String github;
    private String portfolio;
    @Enumerated(EnumType.STRING)
    private Degree degree;

    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    private int attendedYears;

    private double GPA;

    @ElementCollection(targetClass = Skills.class)
    @CollectionTable(name = "student_skills", joinColumns = @JoinColumn(name = "student_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "skill")
    private List<Skills> skills;

    @ElementCollection
    @CollectionTable(name = "student_languages", joinColumns = @JoinColumn(name = "student_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @MapKeyColumn(name = "language")
    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private Map<Language, Level> languages;

    // Relationships remain unchanged
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Course> courses = new ArrayList<>();



    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<StudentProject> StudentProjects = new ArrayList<>();

    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private Test test;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Internship> internships = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cv_id")
    private UploadedFile cv;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cover_letter_id")
    private UploadedFile coverLetter;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "college_id")//FK in Student table
    private College college;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Task> tasks = new ArrayList<>();


    @OneToOne(mappedBy = "student", cascade = CascadeType.ALL)
    private InternRoom internRoom;



    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)

    private List<Rating> ratings = new ArrayList<>();







}