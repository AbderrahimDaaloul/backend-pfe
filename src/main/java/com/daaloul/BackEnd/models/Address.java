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
@Table(name = "addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)  // Uses UUID natively in Spring Boot 3+
    private UUID id;

    private String street;
    private String city;

    @Column(length = 10) // Ensures postal code doesn't exceed expected length
    private String postalCode;


    @OneToOne(mappedBy = "address")
    private Student student;





}
