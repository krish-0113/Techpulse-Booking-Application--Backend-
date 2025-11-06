package com.bookstore.bookstore_assignment.service;

import com.bookstore.bookstore_assignment.dtos.BookRequestDTO;
import com.bookstore.bookstore_assignment.dtos.BookResponseDTO;

import java.util.List;

public interface BookService {
    BookResponseDTO addBook(BookRequestDTO dto);
    BookResponseDTO updateBook(Long id, BookRequestDTO dto);
    void deleteBook(Long id);
    List<BookResponseDTO> getAllBooks();
    BookResponseDTO getBookById(Long id);

    // Manager-specific
    BookResponseDTO updateStock(Long id, Integer newQuantity);

    // Customer-specific
    List<BookResponseDTO> searchBooks(String keyword);
}
