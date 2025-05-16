package com.daaloul.BackEnd.services;

import com.daaloul.BackEnd.DTOs.CourseDTO;
import com.daaloul.BackEnd.DTOs.ESupervisorRegistrationDTO;
import com.daaloul.BackEnd.DTOs.InternshipDTO;
import com.daaloul.BackEnd.DTOs.PSupervisorRegistrationDTO;
import com.daaloul.BackEnd.DTOs.StudentProjectDTO;
import com.daaloul.BackEnd.DTOs.StudentRegistrationDTO;
import com.daaloul.BackEnd.DTOs.UserRegistrationDTO;
import com.daaloul.BackEnd.enums.FileCategory;
import com.daaloul.BackEnd.enums.FileType;
import com.daaloul.BackEnd.enums.Role;
import com.daaloul.BackEnd.models.*;
import com.daaloul.BackEnd.repos.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AdminService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private  StudentRepo studentRepo;

    @Autowired
    private  AddressRepo addressRepo;

    @Autowired
    private  CollegeRepo collegeRepo;

    @Autowired
    private  UploadedFileRepo uploadedFileRepo;

    @Autowired
    private  CourseRepo courseRepo;

    @Autowired
    private  InternshipRepo internshipRepo;

    @Autowired
    private  StudentProjectRepo studentProjectRepo;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    @Autowired
    private StudentRepo studentRepository;

    @Autowired
    private PSupervisorRepo pSupervisorRepository;

    @Autowired
    private ESupervisorRepo eSupervisorRepository;

    @Value("${upload.dir}") // Inject the directory path where you want to store files
    private String uploadDir;


    public User registerAdmin(User user) {
        // Encode password before saving the user
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(Role.ADMIN);

        return userRepo.save(user);
    }




    public User registerUM(User user) {
        // Encode password before saving the user
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(Role.UM);
        return userRepo.save(user);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public Optional<User> findById(UUID id) {
        return userRepo.findById(id);
    }

    public User save(User user) {

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }



    public void delete(User user) {
        userRepo.delete(user);
    }


    public AuthenticationResponse verify(User user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword())
            );

            if (authentication.isAuthenticated()) {
                UserPrincipal userDetails = (UserPrincipal) authentication.getPrincipal();
                User authenticatedUser = userDetails.getUser(); // Requires UserPrincipal to expose the User object

                String token = jwtService.generateToken(
                        authenticatedUser.getEmail(),
                        authenticatedUser.getId(),
                        authenticatedUser.getRole().name() // Ensure the Role is an enum with a name() method
                );
                
                // Return a response object instead of just the token string
                return new AuthenticationResponse(
                    token,
                    authenticatedUser.getRole().name(),
                    authenticatedUser.getEmail(),
                    authenticatedUser.getId()
                );
            } else {
                throw new AuthenticationException("Authentication failed") {
                    private static final long serialVersionUID = 1L;
                };
            }
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }

//    public User registerUser(UserRegistrationDTO userDto) {
//        // Encrypt the password before saving
//        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
//
//        User user = null;
//
//        switch (userDto.getRole()) {
//            case STUDENT:
//                if (userDto instanceof StudentRegistrationDTO studentDto) {
//                    // Step 1: Create User (Base Class)
//                    Student student = new Student();
//                    student.setName(studentDto.getName());
//                    student.setEmail(studentDto.getEmail());
//                    student.setPhone(studentDto.getPhone());
//                    student.setRole(Role.STUDENT);
//                    student.setPassword(bCryptPasswordEncoder.encode(studentDto.getPassword()));
//
//                    // Step 2: Add Student-Specific Fields
//                    student.setBirthDate(studentDto.getBirthDate());
//                    student.setLinkedIn(studentDto.getLinkedIn());
//                    student.setGithub(studentDto.getGithub());
//                    student.setPortfolio(studentDto.getPortfolio());
//                    student.setGPA(studentDto.getGpa());
//                    student.setAttendedYears(studentDto.getAttendedYears());
//                    student.setDegree(studentDto.getDegree());
//                    student.setSpeciality(studentDto.getSpeciality());
//
//                    // Step 3: Save Address (if provided)
//                    if (studentDto.getAddress() != null) {
//                        Address address = new Address();
//                        address.setCity(studentDto.getAddress().getCity());
//                        address.setStreet(studentDto.getAddress().getStreet());
//                        address.setPostalCode(studentDto.getAddress().getPostalCode());
//                        address = addressRepo.save(address); // Save in DB
//                        student.setAddress(address);
//                    }
//
//                    // Step 4: Check if College Exists, Otherwise Create
//                    if (studentDto.getCollege() != null) {
//                        College college = collegeRepo.findByName(studentDto.getCollege().getName())
//                                .orElseGet(() -> {
//                                    College newCollege = new College();
//                                    newCollege.setName(studentDto.getCollege().getName());
//                                    newCollege.setEmail(studentDto.getCollege().getEmail());
//                                    return collegeRepo.save(newCollege);
//                                });
//                        student.setCollege(college);
//                    }
//
//                    // Step 5: Handle Uploaded Files (CV & Cover Letter)
//                    if (studentDto.getCv() != null) {
//                        UploadedFile cv = new UploadedFile(studentDto.getCv().getType(), studentDto.getCv().getUrl(), studentDto.getCv().getVersion());
//                        student.setCv(uploadedFileRepo.save(cv));
//                    }
//                    if (studentDto.getCoverLetter() != null) {
//                        UploadedFile coverLetter = new UploadedFile(studentDto.getCoverLetter().getType(), studentDto.getCoverLetter().getUrl(), studentDto.getCoverLetter().getVersion());
//                        student.setCoverLetter(uploadedFileRepo.save(coverLetter));
//                    }
//
//                    // Step 6: Save Student Entity
//                    student = studentRepo.save(student);
//
//                    // Step 7: Save Skills
//                    student.setSkills(studentDto.getSkills());
//
//                    // Step 8: Save Languages
//                    student.setLanguages(studentDto.getLanguages());
//
//                    // Step 9: Save Courses
//                    if (studentDto.getCourses() != null) {
//                        List<Course> courses = studentDto.getCourses().stream()
//                                .map(c -> new Course(c.getName(), c.getDescription(), c.getCertificationLink(), student))
//                                .collect(Collectors.toList());
//                        courseRepo.saveAll(courses);
//                    }
//
//                    // Step 10: Save Internships
//                    if (studentDto.getInternships() != null) {
//                        List<Internship> internships = studentDto.getInternships().stream()
//                                .map(i -> new Internship(i.getCompany(), i.getDescription(), i.getStartingDate(), i.getEndingDate(), student))
//                                .collect(Collectors.toList());
//                        internshipRepo.saveAll(internships);
//                    }
//
//                    // Step 11: Save Student Projects
//                    if (studentDto.getProjects() != null) {
//                        List<StudentProject> projects = studentDto.getProjects().stream()
//                                .map(p -> new StudentProject(p.getName(), p.getDescription(), p.getGithubLink(), student))
//                                .collect(Collectors.toList());
//                        studentProjectRepo.saveAll(projects);
//                    }
//                    user = studentRepository.save(student);
//                }
//                break;
//
//            case PS:
//                if (userDto instanceof PSupervisorRegistrationDTO pSupervisorDto) {
//                    PSupervisor pSupervisor = new PSupervisor();
//                    BeanUtils.copyProperties(pSupervisorDto, pSupervisor);
//                    user = pSupervisorRepository.save(pSupervisor);
//                }
//                break;
//
//            case ES:
//                if (userDto instanceof ESupervisorRegistrationDTO eSupervisorDto) {
//                    ESupervisor eSupervisor = new ESupervisor();
//                    BeanUtils.copyProperties(eSupervisorDto, eSupervisor);
//                    user = eSupervisorRepository.save(eSupervisor);
//                }
//                break;
//
//            default:
//                throw new IllegalArgumentException("Invalid role type.");
//        }
//
//        return user;
//    }
//public User registerStudent(UserRegistrationDTO userDto, MultipartFile cvFile, MultipartFile coverLetterFile) throws IOException {
//    // Encrypt the password before saving
//    userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
//
//    User user = null;
//
//    switch (userDto.getRole()) {
//        case STUDENT:
//            if (userDto instanceof StudentRegistrationDTO studentDto) {
//                // Step 1: Create and Save Student
//                Student student = new Student();
//                BeanUtils.copyProperties(studentDto, student);
//                student.setRole(Role.STUDENT);
//
//                // Save Address if provided
//                if (studentDto.getAddress() != null) {
//                    Address address = new Address();
//                    BeanUtils.copyProperties(studentDto.getAddress(), address);
//                    address = addressRepo.save(address);
//                    student.setAddress(address);
//                }
//
//                // Save College if provided
//                if (studentDto.getCollege() != null) {
//                    College college = collegeRepo.findByName(studentDto.getCollege().getName())
//                            .orElseGet(() -> {
//                                College newCollege = new College();
//                                BeanUtils.copyProperties(studentDto.getCollege(), newCollege);
//                                return collegeRepo.save(newCollege);
//                            });
//                    student.setCollege(college);
//                }
//
//                // Save CV and Cover Letter
//                if (cvFile != null && !cvFile.isEmpty()) {
//                    String fileName = UUID.randomUUID().toString() + "_" + cvFile.getOriginalFilename();
//
//                    // Define the file path
//                    Path path = Paths.get(uploadDir, fileName);
//
//                    // Create directories if they don't exist
//                    Files.createDirectories(path.getParent());
//
//                    // Save the file on the filesystem
//                    cvFile.transferTo(path.toFile());
//
//                    // Prepare the file URL (can be adjusted if you want to make it accessible via a web URL)
//                    String fileUrl = path.toString();
//                    UploadedFile cv = new UploadedFile();
//                    cv.setType(FileType.PDF); // Or infer from the file, depending on your use case
//                    cv.setUrl(fileUrl);
//                    cv.setFileCategory(FileCategory.CV);
//                    cv= uploadedFileRepo.saveAndFlush(cv);
//                    student.setCv(cv);
//                }
//
//                if (coverLetterFile != null && !coverLetterFile.isEmpty()) {
//                    String fileName = UUID.randomUUID().toString() + "_" + coverLetterFile.getOriginalFilename();
//
//                    // Define the file path
//                    Path path = Paths.get(uploadDir, fileName);
//
//                    // Create directories if they don't exist
//                    Files.createDirectories(path.getParent());
//
//                    // Save the file on the filesystem
//                    coverLetterFile.transferTo(path.toFile());
//                    String fileUrl = path.toString();
//
//
//                    UploadedFile coverLetter = new UploadedFile();
//                    coverLetter.setType(FileType.PDF); // Or infer from the file, depending on your use case
//                    coverLetter.setUrl(fileUrl);
//                    coverLetter.setFileCategory(FileCategory.COVER_LETTER);
//                    coverLetter= uploadedFileRepo.saveAndFlush(coverLetter);
//                    student.setCoverLetter(coverLetter);
//                }
//
//                // Ensure GPA is set correctly
//                student.setGPA(studentDto.getGpa() != null ? studentDto.getGpa() : 0.0);
//
//                // Step 2: Save Student FIRST before using it in relations
//                final Student savedStudent = studentRepository.save(student);
//
//                // Step 3: Save Courses
//                if (studentDto.getCourses() != null && !studentDto.getCourses().isEmpty()) {
//                    List<Course> courses = studentDto.getCourses().stream()
//                            .map(courseDto -> {
//                                Course course = new Course();
//                                BeanUtils.copyProperties(courseDto, course);
//                                course.setStudent(savedStudent);
//                                return course;
//                            })
//                            .toList();
//                    courseRepo.saveAll(courses);
//                }
//
//                // Step 4: Save Internships
//                if (studentDto.getInternships() != null && !studentDto.getInternships().isEmpty()) {
//                    List<Internship> internships = studentDto.getInternships().stream()
//                            .map(internshipDto -> {
//                                Internship internship = new Internship();
//                                BeanUtils.copyProperties(internshipDto, internship);
//                                internship.setStudent(savedStudent);
//                                internship.setStartingDate(internshipDto.getStartingDate()); // Ensure date is set
//                                internship.setEndingDate(internshipDto.getEndingDate()); // Ensure date is set
//                                return internship;
//                            })
//                            .toList();
//                    internshipRepo.saveAll(internships);
//                }
//
//                // Step 5: Save Student Projects
//                if (studentDto.getProjects() != null && !studentDto.getProjects().isEmpty()) {
//                    List<StudentProject> projects = studentDto.getProjects().stream()
//                            .map(projectDto -> {
//                                StudentProject project = new StudentProject();
//                                BeanUtils.copyProperties(projectDto, project);
//                                project.setStudent(savedStudent);
//                                return project;
//                            })
//                            .toList();
//                    studentProjectRepo.saveAll(projects);
//                }
//
//                // Step 6: Fetch saved student including relationships before returning
//                savedStudent.setCourses(courseRepo.findByStudent(savedStudent));
//                savedStudent.setInternships(internshipRepo.findByStudent(savedStudent));
//                savedStudent.setStudentProjects(studentProjectRepo.findByStudent(savedStudent));
//
//                // Ensure CV and Cover Letter are properly retrieved
//                savedStudent.setCv(uploadedFileRepo.findById(savedStudent.getCv().getId()).orElse(null));
//                savedStudent.setCoverLetter(uploadedFileRepo.findById(savedStudent.getCoverLetter().getId()).orElse(null));
//
//                user = savedStudent;
//            }
//            break;
//
//        case PS:
//            if (userDto instanceof PSupervisorRegistrationDTO pSupervisorDto) {
//                PSupervisor pSupervisor = new PSupervisor();
//                BeanUtils.copyProperties(pSupervisorDto, pSupervisor);
//                user = pSupervisorRepository.save(pSupervisor);
//            }
//            break;
//
//        case ES:
//            if (userDto instanceof ESupervisorRegistrationDTO eSupervisorDto) {
//                ESupervisor eSupervisor = new ESupervisor();
//                BeanUtils.copyProperties(eSupervisorDto, eSupervisor);
//                user = eSupervisorRepository.save(eSupervisor);
//            }
//            break;
//
//        default:
//            throw new IllegalArgumentException("Invalid role type.");
//    }
//
//    return user;
//}
//
//
//
//    public User registerUser(UserRegistrationDTO userDto){
//        userDto.setPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
//        User user = null;
//        switch (userDto.getRole()) {
//        case PS:
//            if (userDto instanceof PSupervisorRegistrationDTO pSupervisorDto) {
//                PSupervisor pSupervisor = new PSupervisor();
//                BeanUtils.copyProperties(pSupervisorDto, pSupervisor);
//                user = pSupervisorRepository.save(pSupervisor);
//            }
//            break;
//
//        case ES:
//            if (userDto instanceof ESupervisorRegistrationDTO eSupervisorDto) {
//                ESupervisor eSupervisor = new ESupervisor();
//                BeanUtils.copyProperties(eSupervisorDto, eSupervisor);
//                user = eSupervisorRepository.save(eSupervisor);
//            }
//            break;
//
//        default:
//            throw new IllegalArgumentException("Invalid role type.");
//
//    }
//
//        return user;
//    }
@Transactional
public User registerStudent(StudentRegistrationDTO studentDto, MultipartFile cvFile, MultipartFile coverLetterFile) throws IOException {
    try {
        // Encrypt the password before saving
        studentDto.setPassword(bCryptPasswordEncoder.encode(studentDto.getPassword()));

        // Step 1: Create Student
        Student student = new Student();
        BeanUtils.copyProperties(studentDto, student);
        student.setRole(Role.STUDENT);

        // Handle Address
        if (studentDto.getAddress() != null) {
            Address address = new Address();
            BeanUtils.copyProperties(studentDto.getAddress(), address);
            address = addressRepo.save(address);
            student.setAddress(address);
        }

        // Handle College
        if (studentDto.getCollege() != null) {
            College college = collegeRepo.findByName(studentDto.getCollege().getName())
                    .orElseGet(() -> {
                        College newCollege = new College();
                        BeanUtils.copyProperties(studentDto.getCollege(), newCollege);
                        return collegeRepo.save(newCollege);
                    });
            student.setCollege(college);
        }

        // Handle CV and Cover Letter
        if (cvFile != null && !cvFile.isEmpty()) {
            UploadedFile cv = saveFile(cvFile, FileCategory.CV);
            student.setCv(cv);
        }

        if (coverLetterFile != null && !coverLetterFile.isEmpty()) {
            UploadedFile coverLetter = saveFile(coverLetterFile, FileCategory.COVER_LETTER);
            student.setCoverLetter(coverLetter);
        }

        // Set GPA
        student.setGPA(studentDto.getGpa() != null ? studentDto.getGpa() : 0.0);

        // Initialize collections to prevent null pointer exceptions
        student.setCourses(new ArrayList<>());
        student.setInternships(new ArrayList<>());
        student.setStudentProjects(new ArrayList<>());

        // Step 2: First save the student to get an ID
        Student savedStudent = studentRepository.save(student);

        // Step 3: Handle Courses - Create and add to the student's collection
        if (studentDto.getCourses() != null && !studentDto.getCourses().isEmpty()) {
            List<Course> courses = new ArrayList<>();
            for (CourseDTO courseDto : studentDto.getCourses()) {
                Course course = new Course();
                BeanUtils.copyProperties(courseDto, course);
                course.setStudent(savedStudent);
                courses.add(course);
            }
            // Add courses to the student's collection first
            savedStudent.getCourses().addAll(courses);
            courseRepo.saveAll(courses);
        }

        // Step 4: Handle Internships
        if (studentDto.getInternships() != null && !studentDto.getInternships().isEmpty()) {
            List<Internship> internships = new ArrayList<>();
            for (InternshipDTO internshipDto : studentDto.getInternships()) {
                Internship internship = new Internship();
                BeanUtils.copyProperties(internshipDto, internship);
                internship.setStudent(savedStudent);
                internship.setStartingDate(internshipDto.getStartingDate());
                internship.setEndingDate(internshipDto.getEndingDate());
                internships.add(internship);
            }
            // Add internships to the student's collection first
            savedStudent.getInternships().addAll(internships);
            internshipRepo.saveAll(internships);
        }

        // Step 5: Handle Student Projects
        if (studentDto.getProjects() != null && !studentDto.getProjects().isEmpty()) {
            List<StudentProject> projects = new ArrayList<>();
            for (StudentProjectDTO projectDto : studentDto.getProjects()) {
                StudentProject project = new StudentProject();
                BeanUtils.copyProperties(projectDto, project);
                project.setStudent(savedStudent);
                projects.add(project);
            }
            // Add projects to the student's collection first
            savedStudent.getStudentProjects().addAll(projects);
            studentProjectRepo.saveAll(projects);
        }

        // Step 6: Save the student with all relationships updated
        studentRepository.save(savedStudent);

        // Return the fully populated student
        return savedStudent;
    } catch (Exception e) {
        // Log error and throw to ensure transaction is rolled back
        // log.error("Error registering student: " + e.getMessage(), e);
        throw new RuntimeException("Failed to register student: " + e.getMessage(), e);
    }
}


    public User registerProfessionalSupervisor(PSupervisorRegistrationDTO pSupervisorDto) {
        // Encrypt the password before saving
        pSupervisorDto.setPassword(bCryptPasswordEncoder.encode(pSupervisorDto.getPassword()));

        PSupervisor pSupervisor = new PSupervisor();
        BeanUtils.copyProperties(pSupervisorDto, pSupervisor);
        return pSupervisorRepository.save(pSupervisor);
    }


    public User registerEducationalSupervisor(ESupervisorRegistrationDTO eSupervisorDto) {
        // Encrypt the password before saving
        eSupervisorDto.setPassword(bCryptPasswordEncoder.encode(eSupervisorDto.getPassword()));

        ESupervisor eSupervisor = new ESupervisor();
        BeanUtils.copyProperties(eSupervisorDto, eSupervisor);
        return eSupervisorRepository.save(eSupervisor);
    }


    private UploadedFile saveFile(MultipartFile file, FileCategory fileCategory) throws IOException {
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Define the file path
        Path path = Paths.get(uploadDir, fileName);

        // Create directories if they don't exist
        Files.createDirectories(path.getParent());

        // Save the file on the filesystem
        file.transferTo(path.toFile());

        // Prepare the file URL (can be adjusted if you want to make it accessible via a web URL)
        String fileUrl = path.toString();

        UploadedFile uploadedFile = new UploadedFile();
        uploadedFile.setType(FileType.PDF); // Or infer from the file, depending on your use case
        uploadedFile.setUrl(fileUrl);
        uploadedFile.setFileCategory(fileCategory);
        return uploadedFileRepo.saveAndFlush(uploadedFile);
    }

    public Optional<User> getAdminById(UUID id) {
        return userRepo.findAdminById(id);
    }



    // Get all admins
    public List<User> getAllAdmins() {
        return userRepo.findAllAdmins();
    }

    // Update an admin
    public Optional<User> updateAdmin(UUID id, User updatedAdmin) {
        return userRepo.findAdminById(id).map(existingAdmin -> {
            existingAdmin.setName(updatedAdmin.getName());
            existingAdmin.setEmail(updatedAdmin.getEmail());
            // Add other fields to update as needed
            return userRepo.save(existingAdmin);
        });
    }

    // Delete an admin
    public boolean deleteAdmin(UUID id) {
        Optional<User> admin = userRepo.findAdminById(id);
        if (admin.isPresent()) {
            userRepo.delete(admin.get());
            return true;
        }
        return false;
    }



    // Get all UMs
    public List<User> getAllUMs() {
        return userRepo.findAllUMs();
    }

    // Update a UM
    public Optional<User> updateUM(UUID id, User updatedUM) {
        return userRepo.findUMById(id).map(existingUM -> {
            existingUM.setName(updatedUM.getName());
            existingUM.setEmail(updatedUM.getEmail());
            // Add other fields to update as needed
            return userRepo.save(existingUM);
        });
    }

    // Delete a UM
    public boolean deleteUM(UUID id) {
        Optional<User> um = userRepo.findUMById(id);
        if (um.isPresent()) {
            userRepo.delete(um.get());
            return true;
        }
        return false;
    }

    public Optional<User> getUMById(UUID id) {
        return userRepo.findUMById(id);
    }
}
