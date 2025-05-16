package com.daaloul.BackEnd.models;

import com.daaloul.BackEnd.enums.RatingType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "ratings")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Uses UUID for primary key
    @Column(columnDefinition = "UUID", updatable = false, nullable = false) // Ensures the database uses UUID type
    private UUID id;

    private Double PSRating;

    private Double ESRating;

    @Column(length = 500)  // Defines a maximum length for the comment
    private String PSComment;


    @Column(length = 500)  // Defines a maximum length for the comment
    private String ESComment;



    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)  // FK in Rating table
    private Student student;

    @ManyToOne
    @JoinColumn(name = "eSupervisor_id")  // FK in Rating table
    private ESupervisor eSupervisor;


    @ManyToOne
    @JoinColumn(name = "pSupervisor_id")  // FK in Rating table
    private PSupervisor pSupervisor;








}
