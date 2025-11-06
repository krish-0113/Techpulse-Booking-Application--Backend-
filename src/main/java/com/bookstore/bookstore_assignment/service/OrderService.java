package com.bookstore.bookstore_assignment.service;

import com.bookstore.bookstore_assignment.dtos.OrderRequestDTO;
import com.bookstore.bookstore_assignment.dtos.OrderResponseDTO;
import java.util.List;

public interface OrderService {

    /**
     * Place a new order for the authenticated user
     * @param dto order request data
     * @param email user email extracted from JWT
     * @return saved order details
     */
    OrderResponseDTO placeOrder(OrderRequestDTO dto, String email);

    /**
     * Admin/Manager — fetch all orders
     */
    List<OrderResponseDTO> getAllOrders();

    /**
     * User — fetch orders by email
     */
    List<OrderResponseDTO> getOrdersByUserEmail(String email);
}
