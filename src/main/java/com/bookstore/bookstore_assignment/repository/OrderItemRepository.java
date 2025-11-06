package com.bookstore.bookstore_assignment.repository;


import com.bookstore.bookstore_assignment.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {}