package com.daaloul.BackEnd.models;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "intern_rooms")
public class InternRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;
    private Date startDate;
    private Date endDate;

    @OneToOne
    @JoinColumn(name = "student_id", referencedColumnName = "id")
     private Student student;

    @ManyToOne
    @JoinColumn(name = "e_supervisor_id")
    private ESupervisor eSupervisor;

    @ManyToOne
    @JoinColumn(name = "p_supervisor_id")
    private PSupervisor pSupervisor;


    @OneToMany(mappedBy = "internRoom")
    private List<Task> tasks = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "company_project_id", referencedColumnName = "id")
    private CompanyProject companyProject;


    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "chat_room_id", referencedColumnName = "id")
    private ChatRoom chatRoom;






}





