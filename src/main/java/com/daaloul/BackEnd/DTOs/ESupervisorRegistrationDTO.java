package com.daaloul.BackEnd.DTOs;

import com.daaloul.BackEnd.enums.Degree;
import com.daaloul.BackEnd.enums.Speciality;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ESupervisorRegistrationDTO extends UserRegistrationDTO {
    private Degree degree;
    private Speciality speciality;
}

