package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.CompanyProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository

public interface CompanyProjectRepo extends JpaRepository<CompanyProject, UUID> {


}
