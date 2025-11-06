package com.bookstore.bookstore_assignment.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponseDTO {
    private Long id;
    private String name;
    private String biography;
    private String country;
}
