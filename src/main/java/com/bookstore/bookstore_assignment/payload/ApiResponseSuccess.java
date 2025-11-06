package com.bookstore.bookstore_assignment.payload;

import lombok.*;

/**
 * Generic class to wrap API success responses.
 * @param <T> - type of the response data (User, List<User>, etc.)
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ApiResponseSuccess<T> {
    private String message; // success message
    private T data;         // actual response data
}
