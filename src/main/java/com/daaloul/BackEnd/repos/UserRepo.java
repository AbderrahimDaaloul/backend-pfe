package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository

public interface UserRepo extends JpaRepository<User, UUID> {
    User findByName(String name);
    User findByEmail(String email);


    @Query("SELECT u FROM User u WHERE u.id = :id AND u.role = 'ADMIN'")
    Optional<User> findAdminById(@Param("id") UUID id);


    @Query("SELECT u FROM User u WHERE u.role = 'ADMIN'")
    List<User> findAllAdmins();

    @Query("SELECT u FROM User u WHERE u.role = 'UM'")
    List<User> findAllUMs();

    // Fetch a UM by ID
    @Query("SELECT u FROM User u WHERE u.id = :id AND u.role = 'UM'")
    Optional<User> findUMById(@Param("id") UUID id);



}
