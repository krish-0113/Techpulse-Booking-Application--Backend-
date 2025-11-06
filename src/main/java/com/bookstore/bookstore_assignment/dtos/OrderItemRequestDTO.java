package com.bookstore.bookstore_assignment.dtos;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequestDTO {
    private Long bookId;
    private Integer quantity;
}
