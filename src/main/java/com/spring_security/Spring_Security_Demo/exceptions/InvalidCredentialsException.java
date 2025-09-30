package com.spring_security.Spring_Security_Demo.exceptions;

public class InvalidCredentialsException extends RuntimeException{
    public InvalidCredentialsException(String message){
        super(message);
    }
}
