package com.daaloul.BackEnd.controllers;

import com.daaloul.BackEnd.DTOs.RatingRequestES;
import com.daaloul.BackEnd.DTOs.RatingRequestPS;
import com.daaloul.BackEnd.models.Rating;
import com.daaloul.BackEnd.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RatingController {


    @Autowired

    private RatingService ratingService;

    @PostMapping(value = "/rate-student-ps", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rating> rateStudentByPS(
             @RequestBody RatingRequestPS ratingRequest // Use a DTO to encapsulate the input
    ) {
        Rating rating = ratingService.rateStudentByPs(
                ratingRequest.getStudentId(),
                ratingRequest.getPsId(),
                ratingRequest.getRating(),
                ratingRequest.getPsComment()

        );
        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }


    @PostMapping(value = "/rate-student-es", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Rating> rateStudentByEs(
            @RequestBody RatingRequestES ratingRequest // Use a DTO to encapsulate the input
    ) {
        Rating rating = ratingService.rateStudentByEs(
                ratingRequest.getStudentId(),
                ratingRequest.getEsId(),
                ratingRequest.getRating(),
                ratingRequest.getEsComment()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(rating);
    }
}
