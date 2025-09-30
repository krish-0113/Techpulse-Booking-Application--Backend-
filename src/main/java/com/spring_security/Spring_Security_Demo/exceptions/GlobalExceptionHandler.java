package com.spring_security.Spring_Security_Demo.exceptions;

import com.spring_security.Spring_Security_Demo.dtos.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static java.time.LocalTime.now;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionResponse> resourceNotFoundException(ResourceNotFoundException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }

    //for invadli credentials exceptions
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> invalidCredentialsException(InvalidCredentialsException ex) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.UNAUTHORIZED.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(exceptionResponse, HttpStatus.UNAUTHORIZED);
    }

    //user alredy exits exceptions
    @ExceptionHandler(UserAlreadyExitsException.class)
    public ResponseEntity<ExceptionResponse> userAlredyExitsHandler(UserAlreadyExitsException ex){
        ExceptionResponse exceptionResponse = new ExceptionResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()

        );
        return  new ResponseEntity<>(exceptionResponse,HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
