package com.daaloul.BackEnd.DTOs;


import com.daaloul.BackEnd.enums.Skills;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompanyProjectDTO {
    private String name;
    private String description;
    private List<Skills> stack;
    private UUID specificationId;  // This will store the uploaded file's ID
    private String clientName;

}
