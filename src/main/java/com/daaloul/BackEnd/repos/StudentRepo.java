package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository

public interface StudentRepo extends JpaRepository<Student, UUID> {
}
