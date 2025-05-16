package com.daaloul.BackEnd.services;

import com.daaloul.BackEnd.DTOs.StudentRegistrationResponse;
import com.daaloul.BackEnd.DTOs.StudentRegistrationByHimSelfDTO;
import com.daaloul.BackEnd.DTOs.StudentRegistrationDTO;
import com.daaloul.BackEnd.DTOs.StudentRegistrationResponse;
import com.daaloul.BackEnd.enums.FileCategory;
import com.daaloul.BackEnd.enums.FileType;
import com.daaloul.BackEnd.enums.Role;
import com.daaloul.BackEnd.models.*;
import com.daaloul.BackEnd.repos.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
public class StudentService {

    @Value("${upload.dir}") // Inject the directory path where you want to store files
    private String uploadDir;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private UserRepo userRepo; // Add UserRepo to update User fields

    @Autowired
    private UploadedFileRepo uploadedFileRepo;

    @Autowired
    private StudentRepo studentRepository;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private InternshipRepo internshipRepo;

    @Autowired
    private StudentProjectRepo studentProjectRepo;

    @Autowired
    private CollegeRepo collegeRepo;

    @Autowired
    private AddressRepo addressRepo;





    private final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(12);

    public Student saveStudent(Student student) {
        // Encrypt the password before saving
        student.setPassword(bCryptPasswordEncoder.encode(student.getPassword()));
        return studentRepo.save(student);
    }

    public List<Student> findAllStudents() {
        return studentRepo.findAll();
    }

    public Optional<Student> findStudentByID(UUID id) {
        return studentRepo.findById(id);
    }

    public void delete(Student student) {
        studentRepo.delete(student);
    }

    public UploadedFile getStudentCV(UUID studentId) {
        return uploadedFileRepo.findCvByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("CV not found"));
    }

    public UploadedFile getStudentCoverLetter(UUID studentId) {
        return uploadedFileRepo.findCoverLetterByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Cover letter not found"));
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



    public User registerStudentByHimself(StudentRegistrationByHimSelfDTO studentDto, MultipartFile cvFile, MultipartFile coverLetterFile) throws IOException {
        // Encrypt the password before saving

        // Step 1: Create and Save Student
        Student student = new Student();
        BeanUtils.copyProperties(studentDto, student);
        student.setRole(Role.STUDENT);

        // Save Address if provided
        if (studentDto.getAddress() != null) {
            Address address = new Address();
            BeanUtils.copyProperties(studentDto.getAddress(), address);
            address = addressRepo.save(address);
            student.setAddress(address);
        }

        // Save College if provided
        if (studentDto.getCollege() != null) {
            College college = collegeRepo.findByName(studentDto.getCollege().getName())
                    .orElseGet(() -> {
                        College newCollege = new College();
                        BeanUtils.copyProperties(studentDto.getCollege(), newCollege);
                        return collegeRepo.save(newCollege);
                    });
            student.setCollege(college);
        }

        // Save CV and Cover Letter
        if (cvFile != null && !cvFile.isEmpty()) {
            UploadedFile cv = saveFile(cvFile, FileCategory.CV);
            student.setCv(cv);
        }

        if (coverLetterFile != null && !coverLetterFile.isEmpty()) {
            UploadedFile coverLetter = saveFile(coverLetterFile, FileCategory.COVER_LETTER);
            student.setCoverLetter(coverLetter);
        }

        // Ensure GPA is set correctly
        student.setGPA(studentDto.getGpa() != null ? studentDto.getGpa() : 0.0);

        // Step 2: Save Student FIRST before using it in relations
        final Student savedStudent = studentRepository.save(student);

        // Step 3: Save Courses
        if (studentDto.getCourses() != null && !studentDto.getCourses().isEmpty()) {
            List<Course> courses = studentDto.getCourses().stream()
                    .map(courseDto -> {
                        Course course = new Course();
                        BeanUtils.copyProperties(courseDto, course);
                        course.setStudent(savedStudent);
                        return course;
                    })
                    .toList();
            courseRepo.saveAll(courses);
        }

        // Step 4: Save Internships
        if (studentDto.getInternships() != null && !studentDto.getInternships().isEmpty()) {
            List<Internship> internships = studentDto.getInternships().stream()
                    .map(internshipDto -> {
                        Internship internship = new Internship();
                        BeanUtils.copyProperties(internshipDto, internship);
                        internship.setStudent(savedStudent);
                        internship.setStartingDate(internshipDto.getStartingDate()); // Ensure date is set
                        internship.setEndingDate(internshipDto.getEndingDate()); // Ensure date is set
                        return internship;
                    })
                    .toList();
            internshipRepo.saveAll(internships);
        }

        // Step 5: Save Student Projects
        if (studentDto.getProjects() != null && !studentDto.getProjects().isEmpty()) {
            List<StudentProject> projects = studentDto.getProjects().stream()
                    .map(projectDto -> {
                        StudentProject project = new StudentProject();
                        BeanUtils.copyProperties(projectDto, project);
                        project.setStudent(savedStudent);
                        return project;
                    })
                    .toList();
            studentProjectRepo.saveAll(projects);
        }

        // Step 6: Fetch saved student including relationships before returning
        savedStudent.setCourses(courseRepo.findByStudent(savedStudent));
        savedStudent.setInternships(internshipRepo.findByStudent(savedStudent));
        savedStudent.setStudentProjects(studentProjectRepo.findByStudent(savedStudent));

        // Ensure CV and Cover Letter are properly retrieved
        savedStudent.setCv(uploadedFileRepo.findById(savedStudent.getCv().getId()).orElse(null));
        savedStudent.setCoverLetter(uploadedFileRepo.findById(savedStudent.getCoverLetter().getId()).orElse(null));

        return savedStudent;
    }
}
