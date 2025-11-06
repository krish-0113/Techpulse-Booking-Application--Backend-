package com.bookstore.bookstore_assignment.dtos;

import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequestDTO {

    private List<OrderItemRequestDTO> items;
}
