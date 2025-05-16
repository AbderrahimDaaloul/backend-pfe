package com.daaloul.BackEnd.DTOs;

import lombok.Data;

import java.util.UUID;


@Data
public class RatingRequestPS {

    private UUID studentId;
    private UUID psId;
    private Double rating;
    private String psComment;


}
