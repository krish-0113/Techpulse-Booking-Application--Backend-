package com.spring_security.Spring_Security_Demo.exceptions;

public class UserAlreadyExitsException extends RuntimeException{
    public UserAlreadyExitsException(String message){
        super(message);
    }
}
