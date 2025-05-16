package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository

public interface RatingRepo extends JpaRepository<Rating, UUID> {
//    @Query("SELECT r FROM Rating r WHERE r.student.id = :studentId AND r.pSupervisor.id = :pSupervisorId")
//    Optional<Rating> findByStudentAndPSupervisor(@Param("studentId") UUID studentId, @Param("pSupervisorId") UUID pSupervisorId);
//
//
//
//
//    @Query("SELECT r FROM Rating r WHERE r.student.id = :studentId AND r.eSupervisor.id = :eSupervisorId")
//    Optional<Rating> findByStudentAndESupervisor(@Param("studentId") UUID studentId, @Param("eSupervisorId") UUID eSupervisorId);


    Optional<Rating> findByStudent_Id(UUID studentId);}
