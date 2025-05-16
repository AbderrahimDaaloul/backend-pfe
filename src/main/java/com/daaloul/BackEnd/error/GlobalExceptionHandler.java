//package com.daaloul.BackEnd.error;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//
//    // Handle UserNotFoundException
//    @ExceptionHandler(NotFoundUserException.class)
//    public ResponseEntity<ErrorDetails> handleUserNotFoundException(NotFoundUserException ex, HttpServletRequest request) {
//        ErrorDetails error = new ErrorDetails(
//                ex.getMessage(),
//                HttpStatus.NOT_FOUND.value(),
//                request.getRequestURI()
//        );
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }
//
//    // Handle Any Other Exception
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorDetails> handleGeneralException(Exception ex, HttpServletRequest request) {
//        ErrorDetails error = new ErrorDetails(
//                "fama unhandled exception ya ta7foun ",
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                request.getRequestURI()
//        );
//        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//}
