package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository

public interface QuestionRepo extends JpaRepository<Question, UUID> {
}
