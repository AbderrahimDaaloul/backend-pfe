package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.Course;
import com.daaloul.BackEnd.models.Internship;
import com.daaloul.BackEnd.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository

public interface InternshipRepo extends JpaRepository<Internship, UUID> {
    List<Internship> findByStudent(Student student);
}
