//package com.daaloul.BackEnd.models;
//
//import com.daaloul.BackEnd.enums.Role;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "users")
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS) // Use TABLE_PER_CLASS strategy
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
//    private UUID id;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(unique = true, nullable = false)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;
//
//    @Column(nullable = false)
//    private String phone;
//
//    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Message> sentMessages= new ArrayList<>();
//
//    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Message> receivedMessages= new ArrayList<>();
//
//
//
//
//
//
//
//}


package com.daaloul.BackEnd.models;

import com.daaloul.BackEnd.enums.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@Table(name = "users") // Use singular table name
@Inheritance(strategy = InheritanceType.JOINED) // Change to JOINED strategy
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // Use enum for role

    @Column(nullable = false)
    private String phone; // Use this for phone number (no need for duplicates in subclasses)

    // Messaging relationships remain unchanged
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> receivedMessages = new ArrayList<>();
}
