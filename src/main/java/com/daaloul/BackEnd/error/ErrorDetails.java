package com.daaloul.BackEnd.error;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class ErrorDetails {
    private String message;
    private int status;
    private LocalDateTime timestamp;
    private String details;

    public ErrorDetails(String message, int status, String details) {
        this.message = message;
        this.status = status;
        this.details = details;
    }

}

