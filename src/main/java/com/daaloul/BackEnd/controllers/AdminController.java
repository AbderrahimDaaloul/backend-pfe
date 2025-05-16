package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.DTOs.ESupervisorRegistrationDTO;
import com.daaloul.BackEnd.DTOs.PSupervisorRegistrationDTO;
import com.daaloul.BackEnd.DTOs.StudentRegistrationDTO;
import com.daaloul.BackEnd.models.*;
import com.daaloul.BackEnd.error.NotFoundUserException;
import com.daaloul.BackEnd.repos.UploadedFileRepo;
import com.daaloul.BackEnd.services.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private EsService esService;

    @Autowired
    private PsService psService;

    @Autowired
    private UploadFileService uploadedFileService;


    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody User user) {
        try {
            AuthenticationResponse response = adminService.verify(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new AuthenticationResponse(null, null, null, null));
        }
    }


    //admin registration
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/admin")
    public ResponseEntity<User> register(@RequestBody User users) {
        User response = adminService.registerAdmin(users);
        return ResponseEntity.ok(response);
    }



    // Register a student
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/register/student", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @CrossOrigin(origins = "http://localhost:5173")

    public ResponseEntity<?> registerStudent(
            @RequestPart("studentData") String studentDataJson,
            @RequestPart("cv") MultipartFile cv,
            @RequestPart("coverLetter") MultipartFile coverLetter) throws IOException {

        // Parse the JSON string to a StudentRegistrationDTO object
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // For LocalDate support
        StudentRegistrationDTO studentDto;
        try {
            studentDto = objectMapper.readValue(studentDataJson, StudentRegistrationDTO.class);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid JSON data: " + e.getMessage()));
        }

        // Call the registerUser method with the DTO and files
        User registeredUser = adminService.registerStudent(studentDto, cv, coverLetter);

        if (registeredUser != null) {
            return ResponseEntity.ok(registeredUser); // Return the registered user object as JSON
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "Failed to register student"));
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/psupervisor")
    public ResponseEntity<?> registerPSupervisor(@RequestBody PSupervisorRegistrationDTO pSupervisorDto) {
        return ResponseEntity.ok(adminService.registerProfessionalSupervisor(pSupervisorDto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/esupervisor")
    public ResponseEntity<?> registerESupervisor(@RequestBody ESupervisorRegistrationDTO eSupervisorDto) {
        return ResponseEntity.ok(adminService.registerEducationalSupervisor(eSupervisorDto));
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllUsers")
    public List<User> getAllPersons() {
        return adminService.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getUserById/{id}")
    public ResponseEntity<User> getPerson(@PathVariable UUID id) {
        Optional<User> user = adminService.findById(id);
        return user.map(ResponseEntity::ok)
                .orElseThrow(() -> new NotFoundUserException("This user does not exist: " + id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateUser/{id}")
    public ResponseEntity<User> updatePerson(@PathVariable UUID id, @RequestBody User users) {
        return adminService.findById(id)
                .map(existingData -> {
                    existingData.setName(users.getName());
                    existingData.setEmail(users.getEmail());
                    existingData.setPassword(users.getPassword());
                    existingData.setPhone(users.getPhone());
                    adminService.save(existingData);
                    return ResponseEntity.ok(existingData);
                })
                .orElseThrow(() -> new NotFoundUserException("User not found with ID: " + id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable UUID id) {
        return adminService.findById(id)
                .map(user -> {
                    adminService.delete(user);
                    return ResponseEntity.ok("User deleted successfully");
                })
                .orElseThrow(() -> new NotFoundUserException("The user you are trying to delete does not exist: " + id));
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllStudents")
    public List<Student> getAllStudents() {
        return studentService.findAllStudents();
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getStudent/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable UUID id) {
        return studentService.findStudentByID(id)
                .map(ResponseEntity::ok) // If student is found, return it with 200 OK
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id)); // If not found, throw an exception
    }


    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateStudent/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable UUID id, @RequestBody Student studentDetails) {
        return studentService.findStudentByID(id)
                .map(existingStudent -> {
                    // Update User fields
                    existingStudent.setName(studentDetails.getName());
                    existingStudent.setEmail(studentDetails.getEmail());
                    existingStudent.setPhone(studentDetails.getPhone());

                    // Update Student-specific fields
                    existingStudent.setBirthDate(studentDetails.getBirthDate());
                    existingStudent.setDegree(studentDetails.getDegree());
                    existingStudent.setSpeciality(studentDetails.getSpeciality());
                    existingStudent.setGPA(studentDetails.getGPA());

                    // Save the updated student
                    Student updatedStudent = studentService.saveStudent(existingStudent);
                    return ResponseEntity.ok(updatedStudent);
                })
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteStudent/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable UUID id) {
        return studentService.findStudentByID(id)
                .map(Student -> {
                    studentService.delete(Student);
                    return ResponseEntity.ok("User deleted successfully");
                })
                .orElseThrow(() -> new NotFoundUserException("The user you are trying to delete does not exist: " + id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllESupervisors")
    public List<ESupervisor> getAllESupervisors() {
        return esService.findAllESupervisors();
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getESupervisor/{id}")
    public ResponseEntity<ESupervisor> getEducationalSupervisorByID(@PathVariable UUID id) {
        return esService.findESupervisorByID(id)
                .map(ResponseEntity::ok) // If student is found, return it with 200 OK
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id)); // If not found, throw an exception
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updateESupervisor/{id}")
    public ResponseEntity<ESupervisor> updateEducationalSupervisor(@PathVariable UUID id, @RequestBody ESupervisor eSupervisorDetails) {
        return esService.findESupervisorByID(id)
                .map(existingESupervisor -> { // Corrected variable name
                    // Update User fields
                    existingESupervisor.setName(eSupervisorDetails.getName());
                    existingESupervisor.setEmail(eSupervisorDetails.getEmail());
                    existingESupervisor.setPhone(eSupervisorDetails.getPhone());

                    // Update ESupervisor-specific fields
                    existingESupervisor.setDegree(eSupervisorDetails.getDegree());
                    existingESupervisor.setSpeciality(eSupervisorDetails.getSpeciality());

                    // Save the updated ESupervisor
                    ESupervisor updatedESupervisor = esService.saveESupervisor(existingESupervisor); // Corrected variable name
                    return ResponseEntity.ok(updatedESupervisor);
                })
                .orElseThrow(() -> new RuntimeException("ESupervisor not found with ID: " + id)); // Corrected error message
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deleteESupervisor/{id}")
    public ResponseEntity<String> deleteEducationaSupervisor(@PathVariable UUID id) {
        return esService.findESupervisorByID(id)
                .map(Esupervisor -> {
                    esService.delete(Esupervisor);
                    return ResponseEntity.ok("User deleted successfully");
                })
                .orElseThrow(() -> new NotFoundUserException("The user you are trying to delete does not exist: " + id));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllPSupervisors")
    public List<PSupervisor> getAllProfessionalSupervisors() {
        return psService.findAllPSupervisors();
    }



    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getPSupervisor/{id}")
    public ResponseEntity<PSupervisor> getProfessionalSupervisorByID(@PathVariable UUID id) {
        return psService.findPSupervisorByID(id)
                .map(ResponseEntity::ok) // If student is found, return it with 200 OK
                .orElseThrow(() -> new RuntimeException("Professional supervisor not found with ID: " + id)); // If not found, throw an exception
    }



    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/updatePSupervisor/{id}")
    public ResponseEntity<PSupervisor> updateProfessionalSupervisor(@PathVariable UUID id, @RequestBody PSupervisor pSupervisorDetails) {
        PSupervisor existingPSupervisor = psService.findPSupervisorByID(id)
                .orElseThrow(() -> new RuntimeException("Professional Supervisor not found with ID: " + id));

        existingPSupervisor.setName(pSupervisorDetails.getName());
        existingPSupervisor.setEmail(pSupervisorDetails.getEmail());
        existingPSupervisor.setPhone(pSupervisorDetails.getPhone());
        existingPSupervisor.setDegree(pSupervisorDetails.getDegree());
        existingPSupervisor.setDepartment(pSupervisorDetails.getDepartment());

        PSupervisor updatedPSupervisor = psService.savePSupervisor(existingPSupervisor);
        return ResponseEntity.ok(updatedPSupervisor);
    }




    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deletePSupervisor/{id}")
    public ResponseEntity<String> deleteProfessionalSupervisor(@PathVariable UUID id) {
        return psService.findPSupervisorByID(id)
                .map(Psupervisor -> {
                    psService.delete(Psupervisor);
                    return ResponseEntity.ok("User deleted successfully");
                })
                .orElseThrow(() -> new NotFoundUserException("The user you are trying to delete does not exist: " + id));
    }


//    ----------------------------------------------------------------------------------------------------------------

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllCVs")
    public List<UploadedFile> getAllCVs() {
        return uploadedFileService.getAllCVs();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllCover-letters")
    public List<UploadedFile> getAllCoverLetters() {
        return uploadedFileService.getAllCoverLetters();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/getAllSpecifications")
    public List<UploadedFile> getAllSpecifications() {
        return uploadedFileService.getAllSpecifications();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PS')")
    @GetMapping("/{studentId}/cv")
    public ResponseEntity<UploadedFile> getStudentCV(@PathVariable UUID studentId) {
        UploadedFile uploadedFile = studentService.getStudentCV(studentId);
        return ResponseEntity.ok(uploadedFile);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'PS')")
    @GetMapping("/{studentId}/cover-letter")
    public ResponseEntity<UploadedFile> getStudentCoverLetter(@PathVariable UUID studentId) {
        UploadedFile uploadedFile = studentService.getStudentCoverLetter(studentId);
        return ResponseEntity.ok(uploadedFile);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin-profile") // Removed {id} from path
    public ResponseEntity<User> getAdminProfileById(@RequestParam("id") UUID id) { // Changed to @RequestParam
        Optional<User> admin = adminService.getAdminById(id);
        return admin
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    

    @PreAuthorize("hasRole('ADMIN')")
    // Get all admins
    @GetMapping("/admins")
    public ResponseEntity<List<User>> getAllAdmins() {
        List<User> admins = adminService.getAllAdmins();
        if (admins.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(admins);
    }


    @PreAuthorize("hasRole('ADMIN')")
    // Update an admin
    @PutMapping("/updateAdmin/{id}")
    public ResponseEntity<User> updateAdmin(@PathVariable UUID id, @RequestBody User updatedAdmin) {
        Optional<User> admin = adminService.updateAdmin(id, updatedAdmin);
        return admin
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PreAuthorize("hasRole('ADMIN')")
    // Delete an admin
    @DeleteMapping("/deleteAdmin/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable UUID id) {
        boolean isDeleted = adminService.deleteAdmin(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    // Create a UM
    @PostMapping("/register/UM")
    public ResponseEntity<User> createUM(@RequestBody User umUser) {
        User createdUM = adminService.registerUM(umUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUM);
    }


    @PreAuthorize("hasRole('UM')")
    // Get a UM by ID
    @GetMapping("/UM/{id}")
    public ResponseEntity<User> getUMById(@PathVariable UUID id) {
        Optional<User> um = adminService.getUMById(id);
        return um
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }



    @PreAuthorize("hasRole('ADMIN')")
    // Get all UMs
    @GetMapping("/UMs")
    public ResponseEntity<List<User>> getAllUMs() {
        List<User> ums = adminService.getAllUMs();
        if (ums.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(ums);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'UM')")
    @PutMapping("/updateUM/{id}")
    public ResponseEntity<User> updateUM(@PathVariable UUID id, @RequestBody User updatedUM) {
        Optional<User> um = adminService.updateUM(id, updatedUM);
        return um
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PreAuthorize("hasRole('ADMIN')")
    // Delete a UM
    @DeleteMapping("/deleteUM/{id}")
    public ResponseEntity<Void> deleteUM(@PathVariable UUID id) {
        boolean isDeleted = adminService.deleteUM(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
