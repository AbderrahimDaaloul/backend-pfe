package com.daaloul.BackEnd.models;

import com.daaloul.BackEnd.enums.Degree;
import com.daaloul.BackEnd.enums.Speciality;
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
@Entity
@Table(name = "colleges")
public class College {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;



    @OneToMany(mappedBy = "college", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Student> studentList = new ArrayList<>();


}
