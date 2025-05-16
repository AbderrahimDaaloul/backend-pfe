package com.daaloul.BackEnd.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternRoomResponse {
    private UUID id;
    private Date startDate;
    private Date endDate;
    private UserSummaryDTO student;
    private UserSummaryDTO educationalSupervisor;
    private UserSummaryDTO professionalSupervisor;
    private ProjectSummaryDTO companyProject;
    private UUID chatRoomId;
}