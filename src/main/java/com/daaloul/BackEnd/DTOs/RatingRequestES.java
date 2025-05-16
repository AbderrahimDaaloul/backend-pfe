package com.daaloul.BackEnd.DTOs;

import lombok.Data;

import java.util.UUID;


@Data
public class RatingRequestES {
    private UUID studentId;
    private UUID esId;
    private Double rating;
    private String esComment;

}
