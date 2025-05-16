package com.daaloul.BackEnd.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "chat_rooms")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<Message> messages = new ArrayList<>();


    @OneToOne(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
//    @JsonBackReference // Marks this as the "child" side of the relationship
    private InternRoom internRoom;



    public static ChatRoom createInternshipChatRoom(Student student, CompanyProject project) {
        ChatRoom room = new ChatRoom();
        room.setName("Internship: " + student.getName() + " - " + project.getName());
        return room;
    }


}


