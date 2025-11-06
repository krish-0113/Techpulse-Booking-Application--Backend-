package com.bookstore.bookstore_assignment.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDTO {
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private String authorName;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    private String publisher;
    private Integer publishedYear;
}
