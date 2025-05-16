package com.daaloul.BackEnd.services;

import com.daaloul.BackEnd.models.ESupervisor;
import com.daaloul.BackEnd.models.PSupervisor;
import com.daaloul.BackEnd.models.Rating;
import com.daaloul.BackEnd.models.Student;
import com.daaloul.BackEnd.repos.ESupervisorRepo;
import com.daaloul.BackEnd.repos.PSupervisorRepo;
import com.daaloul.BackEnd.repos.RatingRepo;
import com.daaloul.BackEnd.repos.StudentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;


@Service
public class RatingService {


    @Autowired

    private RatingRepo ratingRepo;

    @Autowired
    private StudentRepo studentRepo;

    @Autowired
    private PSupervisorRepo pSupervisorRepo;

    @Autowired
    private ESupervisorRepo eSupervisorRepo;

    public Rating rateStudentByPs(UUID studentId, UUID psId, Double rating, String comment) {
        // Fetch the student and PS entities
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        PSupervisor pSupervisor = pSupervisorRepo.findById(psId)
                .orElseThrow(() -> new RuntimeException("Professional Supervisor not found"));

        // Check if a rating already exists for this student and PS
        Optional<Rating> existingRating = ratingRepo.findByStudent_Id(studentId);
        Rating newRating;
        if (existingRating.isPresent()) {
            // Update the existing rating
            newRating = existingRating.get();
            newRating.setPSRating(rating); // Update the PS rating
            newRating.setPSComment(comment); // Update the comment
            newRating.setPSupervisor(pSupervisor); // Set the PS

            System.out.println("Updating existing rating with comment: " + comment);
            System.out.println("Comment after setting: " + newRating.getPSComment());
        } else {
            // Create a new Rating entity
            newRating = new Rating();
            newRating.setPSRating(rating); // Set the PS rating
            newRating.setPSComment(comment); // Set the comment
            newRating.setStudent(student); // Set the student
            newRating.setPSupervisor(pSupervisor); // Set the PS

            System.out.println("Creating new rating with comment: " + comment);
            System.out.println("Comment after setting: " + newRating.getPSComment());
        }

        // Save the rating
        Rating savedRating = ratingRepo.save(newRating);
        System.out.println("Saved rating with comment: " + savedRating.getPSComment());
        return savedRating;
    }

    public Rating rateStudentByEs(UUID studentId, UUID esId, Double rating, String comment) {
        // Fetch the student and ES entities
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        ESupervisor eSupervisor = eSupervisorRepo.findById(esId)
                .orElseThrow(() -> new RuntimeException("Educational Supervisor not found"));

        Optional<Rating> existingRating = ratingRepo.findByStudent_Id(studentId);

        Rating newRating;
        if (existingRating.isPresent()) {
            // Update the existing rating
            newRating = existingRating.get();
            newRating.setESRating(rating); // Update the ES rating
            newRating.setESComment(comment); // Update the comment
            newRating.setESupervisor(eSupervisor); // Set the ES

            System.out.println("Updating existing rating with ES comment: " + comment);
            System.out.println("ES Comment after setting: " + newRating.getESComment());
        } else {
            // Create a new Rating entity
            newRating = new Rating();
            newRating.setESRating(rating); // Set the ES rating
            newRating.setESComment(comment); // Set the comment
            newRating.setStudent(student); // Set the student
            newRating.setESupervisor(eSupervisor); // Set the ES

            System.out.println("Creating new rating with ES comment: " + comment);
            System.out.println("ES Comment after setting: " + newRating.getESComment());
        }

        // Save the rating
        Rating savedRating = ratingRepo.save(newRating);
        System.out.println("Saved rating with ES comment: " + savedRating.getESComment());
        return savedRating;
    }
}
