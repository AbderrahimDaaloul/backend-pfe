package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.models.CompanyProject;
import com.daaloul.BackEnd.models.InternRoom;
import com.daaloul.BackEnd.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository

public interface InternRoomRepo extends JpaRepository<InternRoom, UUID> {
    Optional<InternRoom> findByStudentId(UUID studentId);

    boolean existsByStudentId(UUID studentId);

    @Query("SELECT ir.companyProject FROM InternRoom ir WHERE ir.student.id = :studentId")
    CompanyProject findCompanyProjectByStudentId(@Param("studentId") UUID studentId);

    @Query("SELECT ir.student FROM InternRoom ir WHERE ir.pSupervisor.id = :supervisorId")
    List<Student> findStudentsByPSupervisorId(@Param("supervisorId") UUID supervisorId);


    @Query("SELECT ir.companyProject FROM InternRoom ir WHERE ir.student.id = :studentId AND ir.pSupervisor.id = :pSupervisorId")
    Optional<CompanyProject> findCompanyProjectByStudentIdAndPSupervisorId(
            @Param("studentId") UUID studentId,
            @Param("pSupervisorId") UUID pSupervisorId
    );


    @Query("SELECT DISTINCT ir.companyProject FROM InternRoom ir WHERE ir.pSupervisor.id = :pSupervisorId")
    List<CompanyProject> findCompanyProjectsByPSupervisorId(@Param("pSupervisorId") UUID pSupervisorId);


    @Query("SELECT DISTINCT ir.student FROM InternRoom ir WHERE ir.eSupervisor.id = :eSupervisorId")
    List<Student> findStudentsByESupervisorId(@Param("eSupervisorId") UUID eSupervisorId);

    @Query("SELECT ir.companyProject FROM InternRoom ir WHERE ir.student.id = :studentId AND ir.eSupervisor.id = :eSupervisorId")
    Optional<CompanyProject> findCompanyProjectByStudentIdAndESupervisorId(
            @Param("studentId") UUID studentId,
            @Param("eSupervisorId") UUID eSupervisorId
    );

    @Query("SELECT DISTINCT ir.companyProject FROM InternRoom ir WHERE ir.eSupervisor.id = :eSupervisorId")
    List<CompanyProject> findCompanyProjectsByESupervisorId(@Param("eSupervisorId") UUID eSupervisorId);


    @Query("SELECT ir.id FROM InternRoom ir WHERE ir.student.id = :studentId AND ir.pSupervisor.id = :psId")
    Optional<UUID> findInternRoomIdByStudentIdAndPSupervisorId(
            @Param("studentId") UUID studentId,
            @Param("psId") UUID psId
    );

    public interface InternRoomRepository extends JpaRepository<InternRoom, UUID> {
        Optional<InternRoom> findByStudentId(UUID studentId);
    }
}
