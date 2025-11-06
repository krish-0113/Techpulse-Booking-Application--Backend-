package com.bookstore.bookstore_assignment.dtos;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponseDTO {
    private String bookTitle;
    private Integer quantity;
    private BigDecimal price;
}
