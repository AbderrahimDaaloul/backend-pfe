package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository

public interface TaskRepo extends JpaRepository<Task, UUID> {

    @Query("SELECT t FROM Task t WHERE t.student.id = :studentId")
    List<Task> findByStudentId(@Param("studentId") UUID studentId);


}
