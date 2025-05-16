package com.daaloul.BackEnd.error;

public class NotFoundUserException extends RuntimeException{

    public NotFoundUserException(String message) {
        super(message);
    }
}
