package com.spring_security.Spring_Security_Demo.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExceptionResponse {
    private int statusCode;
    private String message;
    private LocalDateTime time;
}
