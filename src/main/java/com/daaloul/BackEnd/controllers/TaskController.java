package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.DTOs.TaskScoreRequest;
import com.daaloul.BackEnd.DTOs.TaskSolutionRequest;
import com.daaloul.BackEnd.DTOs.TaskUpdateRequest;
import com.daaloul.BackEnd.models.Task;
import com.daaloul.BackEnd.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PreAuthorize("hasRole('PS')")
    @PostMapping(value = "/createTask", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Task> createTask(
            @RequestParam UUID PSId, // PS ID for whom the task is being created
            @RequestParam UUID studentId, // Student ID for whom the task is being created
            @RequestParam String name, // Task name
            @RequestParam String description, // Task description
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date deadline // Task deadline
    ) {
        // Create a Task object from the request parameters
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        task.setDeadline(deadline);

        // Create the task
        Task createdTask = taskService.createTask(studentId, PSId, task);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }


    @GetMapping("/student-tasks/{studentId}")
    public ResponseEntity<List<Task>> getTasksByStudentId(@PathVariable UUID studentId) {
        List<Task> tasks = taskService.getTasksByStudentId(studentId);
        if (tasks.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(tasks);
    }


    @PreAuthorize("hasRole('PS')")
    @PutMapping(value = "/update-task/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateTask(
            @PathVariable UUID taskId,
            @RequestBody TaskUpdateRequest taskUpdateRequest // Use a DTO to encapsulate the fields
    ) {
        Optional<Task> task = taskService.updateTask(
                taskId,
                taskUpdateRequest.getName(),
                taskUpdateRequest.getDescription(),
                taskUpdateRequest.getDeadline()
        );
        return task
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PreAuthorize("hasRole('PS')")
    @DeleteMapping("/delete-task/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable UUID taskId) {
        boolean isDeleted = taskService.deleteTask(taskId);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


    @PutMapping(value = "/submit-solution/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> submitTaskSolution(
            @PathVariable UUID taskId,
            @RequestBody TaskSolutionRequest taskSolutionRequest // Use a DTO to encapsulate the GitHub URL
    ) {
        Optional<Task> task = taskService.submitTaskSolution(taskId, taskSolutionRequest.getGithubUrl());
        return task
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PutMapping(value = "/update-score/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> updateTaskScore(
            @PathVariable UUID taskId,
            @RequestBody TaskScoreRequest taskScoreRequest // Use a DTO to encapsulate the score
    ) {
        Optional<Task> task = taskService.updateTaskScore(taskId, taskScoreRequest.getScore());
        return task
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    @PutMapping("/update-task-status-to-approved/{taskId}")
    public ResponseEntity<Task> updateTaskStatusToCompleted(@PathVariable UUID taskId) {
        Optional<Task> task = taskService.updateTaskStatusToApproved(taskId);
        return task
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

}
