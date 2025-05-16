package com.daaloul.BackEnd.DTOs;

import com.daaloul.BackEnd.enums.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class StudentRegistrationDTO extends UserRegistrationDTO {
    // User fields (stored in the users table)
    private String name;
    private String email;
    private String password;
    private String phone;
    private Role role;

    // Student-specific fields (stored in the student table)
    private LocalDate birthDate;
    private String linkedIn;
    private String github;
    private String portfolio;
    private Double gpa;
    private int attendedYears;
    private Degree degree;
    private Speciality speciality;

    // Address details (stored in the addresses table)
    private AddressDTO address;

    // College details (stored in the colleges table)
    private CollegeDTO college;

    // Uploaded files for CV & Cover Letter
    private UploadedFileDTO cv;
    private UploadedFileDTO coverLetter;

    // Skills & Languages
    private List<Skills> skills;
    private Map<Language, Level> languages;

    // Relationships
    private List<CourseDTO> courses;
    private List<InternshipDTO> internships;
    private List<StudentProjectDTO> projects;
}

