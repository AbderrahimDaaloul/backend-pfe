package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.College;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository

public interface CollegeRepo extends JpaRepository<College, UUID> {

    Optional<College> findByName(String name);

}


