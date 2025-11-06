package com.bookstore.bookstore_assignment.service.impl;

import com.bookstore.bookstore_assignment.dtos.OrderItemResponseDTO;
import com.bookstore.bookstore_assignment.dtos.OrderRequestDTO;
import com.bookstore.bookstore_assignment.dtos.OrderResponseDTO;
import com.bookstore.bookstore_assignment.entity.Book;
import com.bookstore.bookstore_assignment.entity.Order;
import com.bookstore.bookstore_assignment.entity.OrderItem;
import com.bookstore.bookstore_assignment.entity.User;
import com.bookstore.bookstore_assignment.exceptions.ResourceNotFoundException;
import com.bookstore.bookstore_assignment.repository.BookRepository;
import com.bookstore.bookstore_assignment.repository.OrderItemRepository;
import com.bookstore.bookstore_assignment.repository.OrderRepository;
import com.bookstore.bookstore_assignment.repository.UserRepository;
import com.bookstore.bookstore_assignment.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final OrderItemRepository orderItemRepository;

    /**
     * 🧑‍💻 Place a new order for the authenticated user (using JWT email)
     */
    @Override
    public OrderResponseDTO placeOrder(OrderRequestDTO dto, String email) {

        // 1️⃣ Fetch the logged-in user using email from JWT token
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        // 2️⃣ Create a new Order object (empty initially)
        Order order = Order.builder()
                .user(user)
                .status("PENDING")
                .build();

        // 3️⃣ Convert DTO items → OrderItem entities (linking order immediately)
        Set<OrderItem> items = dto.getItems().stream().map(itemDto -> {

            // Fetch book
            Book book = bookRepository.findById(itemDto.getBookId())
                    .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + itemDto.getBookId()));

            // Check stock availability
            if (book.getStockQuantity() < itemDto.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for book: " + book.getTitle());
            }

            // Deduct stock and update DB
            book.setStockQuantity(book.getStockQuantity() - itemDto.getQuantity());
            bookRepository.save(book);

            // ✅ Create OrderItem and link directly to Order (no modification later)
            return OrderItem.builder()
                    .order(order)
                    .book(book)
                    .quantity(itemDto.getQuantity())
                    .priceAtPurchase(book.getPrice())
                    .build();

        }).collect(Collectors.toSet());

        // 4️⃣ Calculate total price
        BigDecimal totalPrice = items.stream()
                .map(i -> i.getPriceAtPurchase().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5️⃣ Set items and total price
        order.setItems(items);
        order.setTotalPrice(totalPrice);

        // 6️⃣ Save the order (cascade will save items automatically)
        Order savedOrder = orderRepository.save(order);

        // 7️⃣ Convert to DTO
        return mapToDTO(savedOrder);
    }

    /**
     * 👩‍💼 ADMIN / MANAGER — View all orders
     */
    @Override
    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 🧑‍💻 CUSTOMER — View orders by email
     */
    @Override
    public List<OrderResponseDTO> getOrdersByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));

        return orderRepository.findByUser(user)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 🔄 Convert Order → OrderResponseDTO
     */
    private OrderResponseDTO mapToDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .username(order.getUser().getEmail())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .totalPrice(order.getTotalPrice())
                .items(order.getItems().stream()
                        .map(i -> OrderItemResponseDTO.builder()
                                .bookTitle(i.getBook().getTitle())
                                .quantity(i.getQuantity())
                                .price(i.getPriceAtPurchase())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
