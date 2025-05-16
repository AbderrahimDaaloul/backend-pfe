package com.daaloul.BackEnd.models;

import com.daaloul.BackEnd.enums.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class PSupervisor extends User {

    @Enumerated(EnumType.STRING)
    private Degree degree;

    @Enumerated(EnumType.STRING)
    private Department department;


    @OneToMany(mappedBy = "pSupervisor", cascade = CascadeType.ALL)
    private List<InternRoom> internRooms;

    @OneToMany(mappedBy = "pSupervisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();
}