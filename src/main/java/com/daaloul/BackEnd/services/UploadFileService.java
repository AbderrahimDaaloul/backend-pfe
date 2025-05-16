package com.daaloul.BackEnd.services;
import com.daaloul.BackEnd.enums.FileCategory;
import com.daaloul.BackEnd.models.UploadedFile;
import com.daaloul.BackEnd.repos.UploadedFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UploadFileService {


    @Autowired
    private UploadedFileRepo uploadedFileRepo;

    public List<UploadedFile> getAllCVs() {
        return uploadedFileRepo.findByFileCategory(FileCategory.CV);
    }

    public List<UploadedFile> getAllCoverLetters() {
        return uploadedFileRepo.findByFileCategory(FileCategory.COVER_LETTER);
    }

    public List<UploadedFile> getAllSpecifications() {
        return uploadedFileRepo.findByFileCategory(FileCategory.SPECIFICATION);
    }


}
