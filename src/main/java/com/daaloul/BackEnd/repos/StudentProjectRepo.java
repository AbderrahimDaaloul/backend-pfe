package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.Internship;
import com.daaloul.BackEnd.models.Student;
import com.daaloul.BackEnd.models.StudentProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentProjectRepo extends JpaRepository<StudentProject, UUID> {
    List<StudentProject> findByStudent(Student student);

}
