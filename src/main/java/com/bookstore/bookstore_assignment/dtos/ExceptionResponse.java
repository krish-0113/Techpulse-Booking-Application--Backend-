package com.bookstore.bookstore_assignment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExceptionResponse {
    private int statusCode;
    private String error;
    private String message;
    private String path;
    private LocalDateTime timestamp;
}
