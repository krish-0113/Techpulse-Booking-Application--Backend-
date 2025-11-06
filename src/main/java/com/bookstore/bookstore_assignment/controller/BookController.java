package com.bookstore.bookstore_assignment.controller;

import com.bookstore.bookstore_assignment.dtos.BookRequestDTO;
import com.bookstore.bookstore_assignment.dtos.BookResponseDTO;
import com.bookstore.bookstore_assignment.payload.ApiResponseSuccess;
import com.bookstore.bookstore_assignment.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for managing books (Admin/Manager/User)
 */
@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // ✅ Add new book (Admin or Manager only)
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PostMapping()
    public ResponseEntity<ApiResponseSuccess<BookResponseDTO>> addBook(@Valid @RequestBody BookRequestDTO request) {
        BookResponseDTO response = bookService.addBook(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseSuccess<>("Book added successfully!", response));
    }

    // ✅ Update book details
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<BookResponseDTO>> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequestDTO request) {

        BookResponseDTO response = bookService.updateBook(id, request);
        return ResponseEntity.ok(new ApiResponseSuccess<>("Book updated successfully!", response));
    }

    // ✅ Delete book by ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<Void>> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponseSuccess<>("Book deleted successfully!", null));
    }

    // ✅ Get all books (accessible to all authenticated users)
    @GetMapping
    public ResponseEntity<ApiResponseSuccess<List<BookResponseDTO>>> getAllBooks() {
        List<BookResponseDTO> books = bookService.getAllBooks();
        return ResponseEntity.ok(new ApiResponseSuccess<>("Books fetched successfully!", books));
    }

    // ✅ Get single book by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<BookResponseDTO>> getBookById(@PathVariable Long id) {
        BookResponseDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(new ApiResponseSuccess<>("Book fetched successfully!", book));
    }

    // ✅ Search book (title, author, isbn)
    @GetMapping("/search")
    public ResponseEntity<ApiResponseSuccess<List<BookResponseDTO>>> searchBooks(@RequestParam String keyword) {
        List<BookResponseDTO> result = bookService.searchBooks(keyword);
        return ResponseEntity.ok(new ApiResponseSuccess<>("Search results fetched successfully!", result));
    }

    // ✅ Update only stock quantity
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PatchMapping("/{id}/stock")
    public ResponseEntity<ApiResponseSuccess<BookResponseDTO>> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {

        BookResponseDTO updated = bookService.updateStock(id, quantity);
        return ResponseEntity.ok(new ApiResponseSuccess<>("Stock updated successfully!", updated));
    }
}
