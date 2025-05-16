package com.daaloul.BackEnd.services;

import com.daaloul.BackEnd.enums.Status;
import com.daaloul.BackEnd.models.InternRoom;
import com.daaloul.BackEnd.models.Student;
import com.daaloul.BackEnd.models.Task;
import com.daaloul.BackEnd.repos.InternRoomRepo;
import com.daaloul.BackEnd.repos.StudentRepo;
import com.daaloul.BackEnd.repos.TaskRepo;
import com.daaloul.BackEnd.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {


    @Autowired
    private InternRoomRepo internRoomRepo;

    @Autowired
    private TaskRepo taskRepository;

    @Autowired
    private StudentRepo studentRepository;


    public Task createTask(UUID studentId, UUID psId, Task task) {
        // Fetch the InternRoom ID for the student and PS
        UUID internRoomId = internRoomRepo.findInternRoomIdByStudentIdAndPSupervisorId(studentId, psId)
                .orElseThrow(() -> new RuntimeException("InternRoom not found for the given student and PS"));

        // Fetch the student
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Set the InternRoom and Student in the task
        InternRoom internRoom = new InternRoom();
        internRoom.setId(internRoomId);

        task.setInternRoom(internRoom);
        task.setStudent(student);

        // Set default status (if needed)
        task.setStatus(Status.PENDING);

        // Save the task
        return taskRepository.save(task);
    }

    public List<Task> getTasksByStudentId(UUID studentId) {
        return taskRepository.findByStudentId(studentId);
    }


    public Optional<Task> updateTask(UUID taskId, String name, String description, Date deadline) {
        return taskRepository.findById(taskId).map(existingTask -> {
            existingTask.setName(name);
            existingTask.setDescription(description);
            existingTask.setDeadline(deadline);
            return taskRepository.save(existingTask);
        });
    }

    public boolean deleteTask(UUID taskId) {
        if (taskRepository.existsById(taskId)) {
            taskRepository.deleteById(taskId);
            return true;
        }
        return false;
    }

    public Optional<Task> submitTaskSolution(UUID taskId, String githubUrl) {
        return taskRepository.findById(taskId).map(existingTask -> {
            existingTask.setSolutionPath(githubUrl); // Set the GitHub URL
            existingTask.setStatus(Status.COMPLETED); // Mark the task as completed
            return taskRepository.save(existingTask);
        });
    }

    public Optional<Task> updateTaskScore(UUID taskId, double score) {
        return taskRepository.findById(taskId).map(existingTask -> {
            existingTask.setScore(score); // Set the score
            return taskRepository.save(existingTask);
        });
    }

    public Optional<Task> updateTaskStatusToApproved(UUID taskId) {

        return taskRepository.findById(taskId).map(existingTask -> {
            existingTask.setStatus(Status.APPROVED); // Mark the task as completed
            return taskRepository.save(existingTask);
        });
    }
}
