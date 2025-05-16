package com.daaloul.BackEnd.services;

import com.daaloul.BackEnd.models.Question;
import com.daaloul.BackEnd.models.Student;
import com.daaloul.BackEnd.models.Test;
import com.daaloul.BackEnd.repos.QuestionRepo;
import com.daaloul.BackEnd.repos.StudentRepo;
import com.daaloul.BackEnd.repos.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TestService {

    @Autowired
    private TestRepo testRepository;

    @Autowired
    private QuestionRepo questionRepository;

    @Autowired
    private StudentRepo studentRepo;

    /**
     * Saves a test along with its questions and calculates the average score.
     *
     * @param studentId The ID of the student taking the test.
     * @param questions The list of questions with student answers and scores.
     * @return The saved Test object.
     */
    @Transactional
    public Test saveTest(UUID studentId, List<Question> questions) {
        // Fetch the existing student
        Student student = studentRepo.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + studentId));

        // Create a new Test object
        Test test = new Test();
        test.setStudent(student); // Link the existing student to the test

        // Save the test first to generate the ID
        test = testRepository.save(test);

        // Calculate the average score
        double totalScore = 0;
        for (Question question : questions) {
            question.setTest(test); // Link the question to the test
            totalScore += question.getScore();
        }
        System.out.println(questions.size());
        double averageScore = totalScore / questions.size();
        test.setResult(averageScore);

        // Save the questions
        questionRepository.saveAll(questions);

        // Update the test with the calculated result
        return testRepository.save(test);
    }
    /**
     * Retrieves a test by its ID.
     *
     * @param testId The ID of the test.
     * @return The Test object.
     */
    public Test getTestById(UUID testId) {
        return testRepository.findById(testId)
                .orElseThrow(() -> new RuntimeException("Test not found"));
    }
}