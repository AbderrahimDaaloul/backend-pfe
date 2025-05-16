package com.daaloul.BackEnd.services;

import com.daaloul.BackEnd.enums.FileCategory;
import com.daaloul.BackEnd.enums.FileType;
import com.daaloul.BackEnd.enums.Skills;
import com.daaloul.BackEnd.enums.Speciality;
import com.daaloul.BackEnd.models.CompanyProject;
import com.daaloul.BackEnd.models.InternRoom;
import com.daaloul.BackEnd.models.Project;
import com.daaloul.BackEnd.models.UploadedFile;
import com.daaloul.BackEnd.repos.CompanyProjectRepo;
import com.daaloul.BackEnd.repos.InternRoomRepo;
import com.daaloul.BackEnd.repos.ProjectRepo;
import com.daaloul.BackEnd.repos.UploadedFileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

@Service
public class CompanyProjectService {


    @Value("${upload.dir}") // Inject the directory path where you want to store files
    private String uploadDir;

    @Autowired
    private CompanyProjectRepo companyProjectRepo;
    @Autowired
    private UploadedFileRepo uploadedFileRepo;

    @Autowired
    private InternRoomRepo internRoomRepo;

    public CompanyProject createCompanyProject(String name, String clientName, String description,
                                               List<Skills> stack, MultipartFile specificationFile, Speciality project_speciality) throws IOException {
        // Validate required fields
        if (name == null || clientName == null || specificationFile == null) {
            throw new IllegalArgumentException("Name, Client Name, and Specification file are required.");
        }

        // Store the specification file
        String fileName = UUID.randomUUID().toString() + "_" + specificationFile.getOriginalFilename();

        // Define the file path
        Path path = Paths.get(uploadDir, fileName);

        // Create directories if they don't exist
        Files.createDirectories(path.getParent());

        // Save the file on the filesystem
        specificationFile.transferTo(path.toFile());

        // Prepare the file URL (can be adjusted if you want to make it accessible via a web URL)
        String fileUrl = path.toString();
        // Create UploadedFile entity
        UploadedFile specification = new UploadedFile();

        specification.setType(FileType.PDF); // Or infer from the file, depending on your use case
        specification.setUrl(fileUrl);
        specification.setFileCategory(FileCategory.SPECIFICATION);
         uploadedFileRepo.saveAndFlush(specification);

        // Create and save the CompanyProject
        CompanyProject companyProject = new CompanyProject();
        companyProject.setName(name);
        companyProject.setClientName(clientName);
        companyProject.setDescription(description);
        companyProject.setStack(stack);
        companyProject.setSpecification(specification);
        companyProject.setProject_speciality(project_speciality);

        return companyProjectRepo.save(companyProject);
    }

    public List<CompanyProject> getAllProjects() {
        return companyProjectRepo.findAll();
    }

    public Optional<CompanyProject> getProjectById(UUID id) {
        return companyProjectRepo.findById(id);
    }


    public CompanyProject updateProject(UUID id, String name, String clientName, String description,
                                        List<Skills> stack, MultipartFile specificationFile) throws IOException {
        // Fetch the existing project from the database
        Optional<CompanyProject> existingProjectOpt = companyProjectRepo.findById(id);
        if (existingProjectOpt.isEmpty()) {
            throw new IllegalArgumentException("Project not found with id: " + id);
        }

        CompanyProject existingProject = existingProjectOpt.get();

        // Update project fields
        existingProject.setName(name);
        existingProject.setClientName(clientName);
        existingProject.setDescription(description);
        existingProject.setStack(stack);

        // Handle file upload if a new specification file is provided
        if (specificationFile != null && !specificationFile.isEmpty()) {
            // Generate a new filename for the uploaded specification file
            String fileName = UUID.randomUUID().toString() + "_" + specificationFile.getOriginalFilename();

            // Define the file path
            Path path = Paths.get(uploadDir, fileName);

            // Create directories if they don't exist
            Files.createDirectories(path.getParent());

            // Save the file on the filesystem
            specificationFile.transferTo(path.toFile());

            // Prepare the file URL
            String fileUrl = path.toString();

            // Create the new specification file entity
            UploadedFile newSpecification = new UploadedFile();
            newSpecification.setType(FileType.PDF); // Set file type (you can infer from the file)
            newSpecification.setUrl(fileUrl);
            newSpecification.setFileCategory(FileCategory.SPECIFICATION); // Customize based on your needs
            uploadedFileRepo.saveAndFlush(newSpecification);

            // Set the new specification file to the project
            existingProject.setSpecification(newSpecification);
        }

        // Save the updated project to the database
        return companyProjectRepo.save(existingProject);
    }

    public void deleteProject(UUID id) {
        companyProjectRepo.deleteById(id);
    }

    public void uploadStudentCode(UUID studentId, MultipartFile codeFile) throws IOException {
        // 1. Find the project associated with this student
        InternRoom internRoom = internRoomRepo.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("No project found for this student"));
        
        UUID projectId = internRoom.getCompanyProject().getId();
        CompanyProject project = companyProjectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    
        // 2. Store the code file in student-specific folder
        String studentFolder = "student_" + studentId.toString();
        String fileName = "code_" + System.currentTimeMillis() + ".zip";
        
        Path studentDir = Paths.get(uploadDir, studentFolder);
        Files.createDirectories(studentDir);
        
        Path filePath = studentDir.resolve(fileName);
        codeFile.transferTo(filePath.toFile());
    
        // 3. Create file URL (adjust based on your serving mechanism)
        String fileUrl = "/uploads/" + studentFolder + "/" + fileName;
    
        // 4. Create and save the UploadedFile entity
        UploadedFile codeUploadedFile = new UploadedFile();
        codeUploadedFile.setType(FileType.ZIP);
        codeUploadedFile.setFileCategory(FileCategory.CODE);
        codeUploadedFile.setUrl(fileUrl);
        uploadedFileRepo.save(codeUploadedFile);
    
        // 5. Update the project with code reference
        project.setCode(codeUploadedFile);
        companyProjectRepo.save(project);
    }

    
    public void uploadStudentReport(UUID studentId, MultipartFile reportFile) throws IOException {
        // 1. Find the project associated with this student
        InternRoom internRoom = internRoomRepo.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("No project found for this student"));
        
        UUID projectId = internRoom.getCompanyProject().getId();
        CompanyProject project = companyProjectRepo.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
    
        // 2. Store the report file in student-specific folder
        String studentFolder = "student_" + studentId.toString();
        String fileName = "report_" + System.currentTimeMillis() + 
                         (reportFile.getOriginalFilename().endsWith(".pdf") ? ".pdf" : ".docx");
        
        Path studentDir = Paths.get(uploadDir, studentFolder);
        Files.createDirectories(studentDir);
        
        Path filePath = studentDir.resolve(fileName);
        reportFile.transferTo(filePath.toFile());
    
        // 3. Create file URL (adjust based on your serving mechanism)
        String fileUrl = "/uploads/" + studentFolder + "/" + fileName;
    
        // 4. Create and save the UploadedFile entity
        UploadedFile reportUploadedFile = new UploadedFile();
        reportUploadedFile.setType(reportFile.getContentType().contains("pdf") ? FileType.PDF : FileType.PDF);
        reportUploadedFile.setFileCategory(FileCategory.REPORT);
        reportUploadedFile.setUrl(fileUrl);
        uploadedFileRepo.save(reportUploadedFile);
    
        // 5. Update the project with report reference
        project.setReport(reportUploadedFile);
        companyProjectRepo.save(project);
    }
}
