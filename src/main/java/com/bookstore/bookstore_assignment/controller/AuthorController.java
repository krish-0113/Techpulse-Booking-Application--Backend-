package com.bookstore.bookstore_assignment.controller;

import com.bookstore.bookstore_assignment.dtos.AuthorRequestDTO;
import com.bookstore.bookstore_assignment.dtos.AuthorResponseDTO;
import com.bookstore.bookstore_assignment.payload.ApiResponseSuccess;
import com.bookstore.bookstore_assignment.service.AuthorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService authorService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ApiResponseSuccess<AuthorResponseDTO>> addAuthor(@Valid @RequestBody AuthorRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseSuccess<>("Author added successfully!", authorService.addAuthor(dto)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<AuthorResponseDTO>> updateAuthor(@PathVariable Long id, @RequestBody AuthorRequestDTO dto) {
        return ResponseEntity.ok(new ApiResponseSuccess<>("Author updated successfully!", authorService.updateAuthor(id, dto)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<Void>> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.ok(new ApiResponseSuccess<>("Author deleted successfully!", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponseSuccess<List<AuthorResponseDTO>>> getAllAuthors() {
        return ResponseEntity.ok(new ApiResponseSuccess<>("Authors fetched successfully!", authorService.getAllAuthors()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseSuccess<AuthorResponseDTO>> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(new ApiResponseSuccess<>("Author fetched successfully!", authorService.getAuthorById(id)));
    }
}
