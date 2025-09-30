package com.spring_security.Spring_Security_Demo.exceptions;

public class ResourceNotFoundException extends  RuntimeException {
    public  ResourceNotFoundException(String message){
        super(message);
    }
}
