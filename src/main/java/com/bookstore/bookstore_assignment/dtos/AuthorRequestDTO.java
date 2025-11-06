package com.bookstore.bookstore_assignment.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorRequestDTO {
    @NotBlank(message = "Author name is required")
    private String name;

    private String biography;

    private String country;
}
