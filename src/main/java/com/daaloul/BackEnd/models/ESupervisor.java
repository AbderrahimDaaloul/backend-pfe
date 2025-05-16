package com.daaloul.BackEnd.models;

import com.daaloul.BackEnd.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ESupervisor extends User {

    @Enumerated(EnumType.STRING)
    private Degree degree;

    @Enumerated(EnumType.STRING)
    private Speciality speciality;


    @OneToMany(mappedBy = "eSupervisor", cascade = CascadeType.ALL)
    private List<InternRoom> internRooms;

    @OneToMany(mappedBy = "eSupervisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();
}