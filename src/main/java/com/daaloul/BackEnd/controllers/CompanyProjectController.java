package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.enums.Skills;
import com.daaloul.BackEnd.enums.Speciality;
import com.daaloul.BackEnd.models.CompanyProject;
import com.daaloul.BackEnd.services.CompanyProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class CompanyProjectController {

    @Autowired
    private CompanyProjectService CompanyprojectService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/CreateCompanyProject")
    public ResponseEntity<CompanyProject> createCompanyProject(
            @RequestParam String name,
            @RequestParam String clientName,
            @RequestParam(required = false) String description,
            @RequestParam List<Skills> stack,
            @RequestParam MultipartFile specificationFile,
            @RequestParam Speciality project_speciality) {

        try {
            CompanyProject project = CompanyprojectService.createCompanyProject(name, clientName, description, stack, specificationFile, project_speciality);
            return ResponseEntity.ok(project);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(null);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllCompanyProjects")
    public ResponseEntity<List<CompanyProject>> getAllProjects() {
        return ResponseEntity.ok(CompanyprojectService.getAllProjects());
    }

    @GetMapping("/getCompanyProject/{id}")
    public ResponseEntity<CompanyProject> getProjectById(@PathVariable UUID id) {
        Optional<CompanyProject> project = CompanyprojectService.getProjectById(id);
        return project.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateCompanyProject/{id}")
    public ResponseEntity<CompanyProject> updateProject(
            @PathVariable UUID id,
            @RequestParam String name,
            @RequestParam String clientName,
            @RequestParam String description,
            @RequestParam List<Skills> stack, // List of skills (stack)
            @RequestParam(required = false) MultipartFile specificationFile // Optional file upload
    ) {
        try {
            // Delegate the business logic to the service layer
            CompanyProject updatedProject = CompanyprojectService.updateProject(id, name, clientName, description, stack, specificationFile);
            return ResponseEntity.ok(updatedProject);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteCompanyProject/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable UUID id) {
        CompanyprojectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('STUDENT')")
    @PostMapping("/upload-code")
    public ResponseEntity<String> uploadStudentCode(
            @RequestParam("studentId") UUID studentId,
            @RequestParam("file") MultipartFile codeFile
    ) throws IOException {
        CompanyprojectService.uploadStudentCode(studentId, codeFile);
        return ResponseEntity.ok("Code file uploaded successfully");
    }





@PreAuthorize("hasRole('STUDENT')")
@PostMapping("/upload-report")
public ResponseEntity<String> uploadStudentReport(
        @RequestParam("studentId") UUID studentId,
        @RequestParam("file") MultipartFile reportFile
) throws IOException {
    CompanyprojectService.uploadStudentReport(studentId, reportFile);
    return ResponseEntity.ok("Report file uploaded successfully");
}
}
