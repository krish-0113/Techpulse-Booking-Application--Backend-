package com.bookstore.bookstore_assignment.service;

import com.bookstore.bookstore_assignment.dtos.AuthorRequestDTO;
import com.bookstore.bookstore_assignment.dtos.AuthorResponseDTO;

import java.util.List;

public interface AuthorService {
    AuthorResponseDTO addAuthor(AuthorRequestDTO dto);
    AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO dto);
    void deleteAuthor(Long id);
    List<AuthorResponseDTO> getAllAuthors();
    AuthorResponseDTO getAuthorById(Long id);
}
