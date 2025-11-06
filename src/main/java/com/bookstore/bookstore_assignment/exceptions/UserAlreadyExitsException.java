package com.bookstore.bookstore_assignment.exceptions;

public class UserAlreadyExitsException extends RuntimeException{
    public UserAlreadyExitsException(String message){
        super(message);
    }
}
