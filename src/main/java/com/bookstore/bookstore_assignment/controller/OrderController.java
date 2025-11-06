package com.bookstore.bookstore_assignment.controller;

import com.bookstore.bookstore_assignment.dtos.OrderRequestDTO;
import com.bookstore.bookstore_assignment.dtos.OrderResponseDTO;
import com.bookstore.bookstore_assignment.payload.ApiResponseSuccess;
import com.bookstore.bookstore_assignment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 🧑‍💻 CUSTOMER — Place a new order
     * ✅ Authenticated USER can place order
     * ✅ userId NOT required in request — extracted from JWT
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/place")
    public ResponseEntity<ApiResponseSuccess<OrderResponseDTO>> placeOrder(
            @RequestBody OrderRequestDTO dto,
            Authentication authentication) {

        // 🔹 Extract user email from JWT token
        String email = authentication.getName();

        // 🔹 Service handles saving order
        OrderResponseDTO response = orderService.placeOrder(dto, email);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseSuccess<>("Order placed successfully!", response));
    }

    /**
     * 👩‍💼 ADMIN / MANAGER — View all orders
     * ✅ Restricted to ADMIN or MANAGER
     */
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @GetMapping("/all")
    public ResponseEntity<ApiResponseSuccess<List<OrderResponseDTO>>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(new ApiResponseSuccess<>("Fetched all orders successfully!", orders));
    }

    /**
     * 🧑‍💻 CUSTOMER — View own orders
     * ✅ JWT-based: user sees only their own orders (no path variable)
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<ApiResponseSuccess<List<OrderResponseDTO>>> getMyOrders(Authentication authentication) {
        // 🔹 Extract user email from JWT
        String email = authentication.getName();

        List<OrderResponseDTO> orders = orderService.getOrdersByUserEmail(email);
        return ResponseEntity.ok(new ApiResponseSuccess<>("Fetched your orders successfully!", orders));
    }
}
