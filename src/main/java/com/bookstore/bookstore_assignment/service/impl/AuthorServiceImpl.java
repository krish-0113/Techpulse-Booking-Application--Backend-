package com.bookstore.bookstore_assignment.service.impl;

import com.bookstore.bookstore_assignment.dtos.AuthorRequestDTO;
import com.bookstore.bookstore_assignment.dtos.AuthorResponseDTO;
import com.bookstore.bookstore_assignment.entity.Author;
import com.bookstore.bookstore_assignment.exceptions.ResourceNotFoundException;
import com.bookstore.bookstore_assignment.repository.AuthorRepository;
import com.bookstore.bookstore_assignment.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    @Override
    public AuthorResponseDTO addAuthor(AuthorRequestDTO dto) {
        Author author = Author.builder()
                .name(dto.getName())
                .biography(dto.getBiography())
                .country(dto.getCountry())
                .build();
        authorRepository.save(author);
        return mapToDTO(author);
    }

    @Override
    public AuthorResponseDTO updateAuthor(Long id, AuthorRequestDTO dto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));

        if (dto.getName() != null && !dto.getName().isBlank()) author.setName(dto.getName());
        if (dto.getBiography() != null) author.setBiography(dto.getBiography());
        if (dto.getCountry() != null) author.setCountry(dto.getCountry());

        authorRepository.save(author);
        return mapToDTO(author);
    }

    @Override
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }

    @Override
    public List<AuthorResponseDTO> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuthorResponseDTO getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return mapToDTO(author);
    }

    private AuthorResponseDTO mapToDTO(Author author) {
        return AuthorResponseDTO.builder()
                .id(author.getId())
                .name(author.getName())
                .biography(author.getBiography())
                .country(author.getCountry())
                .build();
    }
}
