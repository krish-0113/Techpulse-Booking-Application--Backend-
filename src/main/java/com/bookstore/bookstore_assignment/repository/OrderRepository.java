package com.bookstore.bookstore_assignment.repository;

import com.bookstore.bookstore_assignment.entity.Order;
import com.bookstore.bookstore_assignment.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Find orders by user entity
     */
    List<Order> findByUser(User user);
}
