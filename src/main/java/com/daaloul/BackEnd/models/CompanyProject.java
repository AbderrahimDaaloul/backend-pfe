package com.daaloul.BackEnd.models;

import com.daaloul.BackEnd.enums.Skills;
import com.daaloul.BackEnd.enums.Speciality;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class CompanyProject extends Project {


//    @ElementCollection(fetch = FetchType.EAGER) // Creates a separate table for list
//    @CollectionTable(name = "company_project_skills", joinColumns = @JoinColumn(name = "company_project_id"))
    @Enumerated(EnumType.STRING) // Store enums as strings
    private List<Skills> stack = new ArrayList<>();


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "report_id", referencedColumnName = "id",nullable = true)
    private UploadedFile report;

    @Column(nullable = false)
    private String clientName;

    @OneToOne(cascade = CascadeType.ALL)  // Assuming code is also an `UploadedFile`
    @JoinColumn(name = "code_id", referencedColumnName = "id",nullable = true)
    private UploadedFile code;


    @OneToOne(mappedBy = "companyProject")
//    @JsonBackReference // Marks this as the "child" side of the relationship
    private InternRoom internRoom;

    @OneToOne(cascade = CascadeType.ALL) // Assumes `UploadedFile` is an entity
    @JoinColumn(name = "specification_id", referencedColumnName = "id")
    private UploadedFile specification;

    @Enumerated(EnumType.STRING)
    private Speciality project_speciality;
}
