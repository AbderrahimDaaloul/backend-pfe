package com.daaloul.BackEnd.DTOs;

import com.daaloul.BackEnd.models.Student;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class InternshipDTO {
    private String company;
    private String description;
    private Date startingDate;
    private Date endingDate;
}

