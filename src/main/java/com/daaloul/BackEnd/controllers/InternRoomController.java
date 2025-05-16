package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.DTOs.InternRoomRequest;
import com.daaloul.BackEnd.DTOs.InternRoomResponse;
import com.daaloul.BackEnd.models.*;
import com.daaloul.BackEnd.repos.*;
import com.daaloul.BackEnd.services.InternRoomService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class InternRoomController {

    private static final Logger logger = LoggerFactory.getLogger(InternRoomController.class);

    private final InternRoomService internRoomService;

    @Autowired
    private InternRoomRepo internRoomRepo;

    @Autowired
    private TaskRepo taskRepository;

    @Autowired
    private ChatRoomRepo chatRoomRepo;

    @Autowired
    public InternRoomController(InternRoomService internRoomService) {
        this.internRoomService = internRoomService;
    }

    @PostMapping("/createInternRoom")
    public ResponseEntity<InternRoomResponse> createInternRoom(@RequestBody InternRoomRequest request)
            throws BadRequestException {
        InternRoomResponse createdInternRoom = internRoomService.createInternRoom(request);
        return new ResponseEntity<>(createdInternRoom, HttpStatus.CREATED);
    }

    @GetMapping("/internRoom/{id}")
    public ResponseEntity<InternRoom> getInternRoomById(@PathVariable UUID id) {
        InternRoom internRoom = internRoomService.getInternRoomById(id);
        return ResponseEntity.ok(internRoom);
    }

    @GetMapping("/internRooms")
    public ResponseEntity<List<InternRoom>> getAllInternRooms() {
        List<InternRoom> internRooms = internRoomService.getAllInternRooms();
        return ResponseEntity.ok(internRooms);
    }

    @GetMapping("/internRoomByStudent/{studentId}")
    public ResponseEntity<InternRoomResponse> getInternRoomByStudentId(@PathVariable UUID studentId) {
        InternRoomResponse internRoomResponse = internRoomService.getInternRoomByStudentId(studentId);
        return ResponseEntity.ok(internRoomResponse);
    }

    @PutMapping("/updateInternRoom/{id}")
    public ResponseEntity<InternRoomResponse> updateInternRoom(@PathVariable UUID id,
            @RequestBody InternRoomRequest request) throws BadRequestException {
        InternRoomResponse updatedInternRoom = internRoomService.updateInternRoom(id, request);
        return ResponseEntity.ok(updatedInternRoom);
    }

    @GetMapping("/companyProject/{studentId}")
    public ResponseEntity<CompanyProject> getCompanyProjectByStudentId(@PathVariable UUID studentId) {
        CompanyProject companyProject = internRoomService.getCompanyProjectByStudentId(studentId);

        if (companyProject == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(companyProject);
    }

    @GetMapping("/students-by-p-supervisor-id/{supervisorId}")
    public ResponseEntity<List<Student>> getStudentBySupervisorId(@PathVariable UUID supervisorId) {
        List<Student> students = internRoomService.getStudentsByProfessionalSupervisorId(supervisorId);
        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/company-project-by-student-and-p-supervisor-id")
    public ResponseEntity<CompanyProject> getCompanyProjectByStudentAndSupervisor(
            @RequestParam UUID studentId,
            @RequestParam UUID pSupervisorId) {
        Optional<CompanyProject> companyProject = internRoomService
                .getCompanyProjectByStudentIdAndPSupervisorId(studentId, pSupervisorId);
        return companyProject
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/company-projects-by-p-supervisor-id")
    public ResponseEntity<List<CompanyProject>> getCompanyProjectsByPSupervisorId(
            @RequestParam UUID pSupervisorId) {
        List<CompanyProject> companyProjects = internRoomService.getCompanyProjectsByPSupervisorId(pSupervisorId);
        if (companyProjects.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(companyProjects);
    }

    @GetMapping("/students-by-e-supervisor-id")
    public ResponseEntity<List<Student>> getStudentsByESupervisorId(
            @RequestParam UUID eSupervisorId) {
        List<Student> students = internRoomService.getStudentsByESupervisorId(eSupervisorId);
        if (students.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(students);
    }

    @PreAuthorize("hasRole('ES')")
    @GetMapping("/company-project-by-student-and-educational-supervisor")
    public ResponseEntity<CompanyProject> getCompanyProjectByStudentAndESupervisor(
            @RequestParam UUID studentId,
            @RequestParam UUID eSupervisorId) {
        Optional<CompanyProject> companyProject = internRoomService
                .getCompanyProjectByStudentIdAndESupervisorId(studentId, eSupervisorId);
        return companyProject
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PreAuthorize("hasRole('ES')")
    @GetMapping("/company-projects-by-educational-supervisor-id")
    public ResponseEntity<List<CompanyProject>> getCompanyProjectsByESupervisorId(
            @RequestParam UUID eSupervisorId) {
        List<CompanyProject> companyProjects = internRoomService.getCompanyProjectsByESupervisorId(eSupervisorId);
        if (companyProjects.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(companyProjects);
    }
}
