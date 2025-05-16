package com.daaloul.BackEnd.DTOs;

import lombok.Data;

import java.util.Date;



@Data
public class TaskUpdateRequest {
    private String name;
    private String description;
    private Date deadline;


}
