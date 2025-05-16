package com.daaloul.BackEnd.DTOs;


import com.daaloul.BackEnd.models.Question;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SaveTestRequest {

    private UUID studentId;
    private List<Question> questions;

}
