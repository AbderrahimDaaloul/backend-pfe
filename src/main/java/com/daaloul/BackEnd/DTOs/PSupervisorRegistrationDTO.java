package com.daaloul.BackEnd.DTOs;

import com.daaloul.BackEnd.enums.Degree;
import com.daaloul.BackEnd.enums.Department;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PSupervisorRegistrationDTO extends UserRegistrationDTO {
    private Degree degree;
    private Department department;
}

