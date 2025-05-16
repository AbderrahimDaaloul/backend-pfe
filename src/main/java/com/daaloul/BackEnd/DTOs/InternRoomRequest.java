package com.daaloul.BackEnd.DTOs;
import lombok.*;

import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class InternRoomRequest {
    private UUID studentId;
    private UUID educationalSupervisorId;
    private UUID professionalSupervisorId;
    private UUID companyProjectId;
    private Date startDate;
    private Date endDate;
}