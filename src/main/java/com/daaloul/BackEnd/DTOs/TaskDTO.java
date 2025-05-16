package com.daaloul.BackEnd.DTOs;

import com.daaloul.BackEnd.enums.Status;
import com.daaloul.BackEnd.models.InternRoom;
import com.daaloul.BackEnd.models.Student;
import jakarta.persistence.*;

import java.util.Date;
import java.util.UUID;

public class TaskDTO {



    private String name;

    private String description;

    private Date deadline;

    private Status status;

    private String solutionPath;

    private double score;


}
