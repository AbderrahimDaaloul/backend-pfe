package com.daaloul.BackEnd.DTOs;
import com.daaloul.BackEnd.enums.Language;
import com.daaloul.BackEnd.enums.Level;
import com.daaloul.BackEnd.enums.Skills;
import com.daaloul.BackEnd.models.Internship;
import com.daaloul.BackEnd.models.Task;
import com.daaloul.BackEnd.models.UploadedFile;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class StudentRequestDTO {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDate birthDate;
    private String linkedIn;
    private String github;
    private String portfolio;
    private CollegeDTO collegeName;

    private AddressDTO address;

    private List<Skills> skills;
    private Map<Language, Level> languages;

    private FileDTO  cvFile;
    private FileDTO  coverLetterFile;

    private List<InternshipDTO> internships;
    private List<TaskDTO> tasks;


}
