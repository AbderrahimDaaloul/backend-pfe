package com.daaloul.BackEnd.repos;

import com.daaloul.BackEnd.enums.FileCategory;
import com.daaloul.BackEnd.models.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository

public interface UploadedFileRepo extends JpaRepository<UploadedFile, UUID> {
    List<UploadedFile> findByFileCategory(FileCategory fileCategory);


    @Query("SELECT s.cv FROM Student s WHERE s.id = :studentId")
    Optional<UploadedFile> findCvByStudentId(@Param("studentId") UUID studentId);


    @Query("SELECT s.coverLetter FROM Student s WHERE s.id = :studentId")
    Optional<UploadedFile> findCoverLetterByStudentId(@Param("studentId") UUID studentId);

}
