package com.bookstore.bookstore_assignment.exceptions;

public class ResourceNotFoundException extends  RuntimeException {
    public  ResourceNotFoundException(String message){
        super(message);
    }
}
