package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository

public interface ProjectRepo extends JpaRepository<Project, UUID> {

}
