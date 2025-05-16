package com.daaloul.BackEnd.services;

import com.daaloul.BackEnd.DTOs.InternRoomRequest;
import com.daaloul.BackEnd.DTOs.InternRoomResponse;
import com.daaloul.BackEnd.DTOs.ProjectSummaryDTO;
import com.daaloul.BackEnd.DTOs.UserSummaryDTO;
import com.daaloul.BackEnd.exception.ResourceNotFoundException;
import com.daaloul.BackEnd.models.*;
import com.daaloul.BackEnd.repos.*;
import jakarta.transaction.Transactional;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class InternRoomService {

    private final InternRoomRepo internRoomRepository;
    private final StudentRepo studentRepository;
    private final ESupervisorRepo educationalSupervisorRepository;
    private final PSupervisorRepo professionalSupervisorRepository;
    private final CompanyProjectRepo companyProjectRepository;
    private final ChatRoomRepo chatRoomRepository;
    private final TaskRepo taskRepository;

    @Autowired
    public InternRoomService(
            InternRoomRepo internRoomRepository,
            StudentRepo studentRepository,
            ESupervisorRepo educationalSupervisorRepository,
            PSupervisorRepo professionalSupervisorRepository,
            CompanyProjectRepo companyProjectRepository,
            ChatRoomRepo chatRoomRepository,TaskRepo taskRepository) {
        this.internRoomRepository = internRoomRepository;
        this.studentRepository = studentRepository;
        this.educationalSupervisorRepository = educationalSupervisorRepository;
        this.professionalSupervisorRepository = professionalSupervisorRepository;
        this.companyProjectRepository = companyProjectRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.taskRepository = taskRepository;
    }

    @Transactional
    public InternRoomResponse createInternRoom(InternRoomRequest request) throws BadRequestException {
        // Validate request data
        validateInternRoomRequest(request);

        // Check if student is already assigned to an internship
        if (internRoomRepository.existsByStudentId(request.getStudentId())) {
            throw new BadRequestException("Student is already assigned to an internship");
        }

        // Fetch all required entities
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

        ESupervisor educationalSupervisor = educationalSupervisorRepository.findById(request.getEducationalSupervisorId())
                .orElseThrow(() -> new ResourceNotFoundException("Educational supervisor not found with id: " + request.getEducationalSupervisorId()));

        PSupervisor professionalSupervisor = professionalSupervisorRepository.findById(request.getProfessionalSupervisorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professional supervisor not found with id: " + request.getProfessionalSupervisorId()));

        CompanyProject companyProject = companyProjectRepository.findById(request.getCompanyProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Company project not found with id: " + request.getCompanyProjectId()));

        // Create and initialize the chat room
        ChatRoom chatRoom = ChatRoom.createInternshipChatRoom(student, companyProject);

        // Save the chat room first
        chatRoom = chatRoomRepository.save(chatRoom);

        // Create and populate the intern room
        InternRoom internRoom = new InternRoom();
        internRoom.setStartDate(request.getStartDate());
        internRoom.setEndDate(request.getEndDate());
        internRoom.setStudent(student);
        internRoom.setESupervisor(educationalSupervisor);
        internRoom.setPSupervisor(professionalSupervisor);
        internRoom.setCompanyProject(companyProject);
        internRoom.setChatRoom(chatRoom);

        // Establish bidirectional relationship
        chatRoom.setInternRoom(internRoom);

        // Save the intern room (will cascade save the chat room)
        InternRoom savedInternRoom = internRoomRepository.save(internRoom);

        // Map to response DTO and return
        return mapToInternRoomResponse(savedInternRoom);
    }

    private void validateInternRoomRequest(InternRoomRequest request) throws BadRequestException {
        if (request.getStartDate() == null) {
            throw new BadRequestException("Start date is required");
        }

        if (request.getEndDate() == null) {
            throw new BadRequestException("End date is required");
        }

        if (request.getEndDate().before(request.getStartDate())) {
            throw new BadRequestException("End date cannot be before start date");
        }

        if (request.getStudentId() == null) {
            throw new BadRequestException("Student ID is required");
        }

        if (request.getEducationalSupervisorId() == null) {
            throw new BadRequestException("Educational supervisor ID is required");
        }

        if (request.getProfessionalSupervisorId() == null) {
            throw new BadRequestException("Professional supervisor ID is required");
        }

        if (request.getCompanyProjectId() == null) {
            throw new BadRequestException("Company project ID is required");
        }
    }

    private InternRoomResponse mapToInternRoomResponse(InternRoom internRoom) {
        InternRoomResponse response = new InternRoomResponse();
        response.setId(internRoom.getId());
        response.setStartDate(internRoom.getStartDate());
        response.setEndDate(internRoom.getEndDate());

        // Map student
        Student student = internRoom.getStudent();
        UserSummaryDTO studentDto = new UserSummaryDTO(
                student.getId(),
                student.getName(),
                student.getEmail()
        );
        response.setStudent(studentDto);

        // Map educational supervisor
        ESupervisor educationalSupervisor = internRoom.getESupervisor();
        UserSummaryDTO educationalSupervisorDto = new UserSummaryDTO(
                educationalSupervisor.getId(),
                educationalSupervisor.getName(),
                educationalSupervisor.getEmail()
        );
        response.setEducationalSupervisor(educationalSupervisorDto);

        // Map professional supervisor
        PSupervisor professionalSupervisor = internRoom.getPSupervisor();
        UserSummaryDTO professionalSupervisorDto = new UserSummaryDTO(
                professionalSupervisor.getId(),
                professionalSupervisor.getName(),
                professionalSupervisor.getEmail()
        );
        response.setProfessionalSupervisor(professionalSupervisorDto);

        // Map company project
        CompanyProject project = internRoom.getCompanyProject();
        ProjectSummaryDTO projectDto = new ProjectSummaryDTO(
                project.getId(),
                project.getName(),
                project.getDescription());
                response.setCompanyProject(projectDto);

        // Set chat room ID
        response.setChatRoomId(internRoom.getChatRoom().getId());

        return response;
    }

    public InternRoomResponse getInternRoomByStudentId(UUID studentId) {
        InternRoom internRoom = internRoomRepository.findByStudentId(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Intern room not found for student with id: " + studentId));

        return mapToInternRoomResponse(internRoom);
    }



    public InternRoom getInternRoomById(UUID id) {
        return internRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intern room not found with id: " + id));
    }


    public List<InternRoom> getAllInternRooms() {
        return internRoomRepository.findAll();
    }

    @Transactional
    public InternRoomResponse updateInternRoom(UUID id, InternRoomRequest request) throws BadRequestException {
        InternRoom existingInternRoom = internRoomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Intern room not found with id: " + id));

        // Validate request
        validateInternRoomRequest(request);

        // Fetch related entities
        Student student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + request.getStudentId()));

        ESupervisor educationalSupervisor = educationalSupervisorRepository.findById(request.getEducationalSupervisorId())
                .orElseThrow(() -> new ResourceNotFoundException("Educational supervisor not found with id: " + request.getEducationalSupervisorId()));

        PSupervisor professionalSupervisor = professionalSupervisorRepository.findById(request.getProfessionalSupervisorId())
                .orElseThrow(() -> new ResourceNotFoundException("Professional supervisor not found with id: " + request.getProfessionalSupervisorId()));

        CompanyProject companyProject = companyProjectRepository.findById(request.getCompanyProjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Company project not found with id: " + request.getCompanyProjectId()));

        // Update fields
        existingInternRoom.setStartDate(request.getStartDate());
        existingInternRoom.setEndDate(request.getEndDate());
        existingInternRoom.setStudent(student);
        existingInternRoom.setESupervisor(educationalSupervisor);
        existingInternRoom.setPSupervisor(professionalSupervisor);
        existingInternRoom.setCompanyProject(companyProject);

        // Save and return response
        InternRoom updatedInternRoom = internRoomRepository.save(existingInternRoom);
        return mapToInternRoomResponse(updatedInternRoom);
    }

//    @Transactional
//    public void deleteInternRoom(UUID id) {
//        // Fetch the intern room
//        InternRoom internRoom = internRoomRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Intern room not found with id: " + id));
//
//        // Delete associated tasks (if any)
//        if (internRoom.getTasks() != null && !internRoom.getTasks().isEmpty()) {
//            taskRepository.deleteAll(internRoom.getTasks());
//        }
//
//        // Delete the associated chat room (if it exists)
//        if (internRoom.getChatRoom() != null) {
//            chatRoomRepository.delete(internRoom.getChatRoom());
//        }
//
//        // Delete the intern room
//        internRoomRepository.delete(internRoom);
//    }


    public CompanyProject getCompanyProjectByStudentId(UUID studentId) {
        return internRoomRepository.findCompanyProjectByStudentId(studentId);
    }


    public List<Student> getStudentsByProfessionalSupervisorId(UUID supervisorId) {
        return internRoomRepository.findStudentsByPSupervisorId(supervisorId);
    }


    public Optional<CompanyProject> getCompanyProjectByStudentIdAndPSupervisorId(UUID studentId, UUID pSupervisorId) {
        return internRoomRepository.findCompanyProjectByStudentIdAndPSupervisorId(studentId, pSupervisorId);
    }

    public List<CompanyProject> getCompanyProjectsByPSupervisorId(UUID pSupervisorId) {
        return internRoomRepository.findCompanyProjectsByPSupervisorId(pSupervisorId);
    }

    public List<Student> getStudentsByESupervisorId(UUID eSupervisorId) {
        return internRoomRepository.findStudentsByESupervisorId(eSupervisorId);
    }

    public Optional<CompanyProject> getCompanyProjectByStudentIdAndESupervisorId(UUID studentId, UUID eSupervisorId) {
        return internRoomRepository.findCompanyProjectByStudentIdAndESupervisorId(studentId, eSupervisorId);
    }

    public List<CompanyProject> getCompanyProjectsByESupervisorId(UUID eSupervisorId) {
        return internRoomRepository.findCompanyProjectsByESupervisorId(eSupervisorId);
    }

}





