package com.bookstore.bookstore_assignment.dtos;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseDTO {
    private Long id;
    private String username;
    private BigDecimal totalPrice;
    private String status;
    private LocalDateTime orderDate;
    private List<OrderItemResponseDTO> items;
}
