package com.daaloul.BackEnd.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDTO {
    private UUID id;
    private String fullName;
    private String email;
}
