package com.bookstore.bookstore_assignment.service.impl;

import com.bookstore.bookstore_assignment.dtos.BookRequestDTO;
import com.bookstore.bookstore_assignment.dtos.BookResponseDTO;
import com.bookstore.bookstore_assignment.entity.Author;
import com.bookstore.bookstore_assignment.entity.Book;
import com.bookstore.bookstore_assignment.exceptions.ResourceNotFoundException;
import com.bookstore.bookstore_assignment.repository.AuthorRepository;
import com.bookstore.bookstore_assignment.repository.BookRepository;
import com.bookstore.bookstore_assignment.service.BookService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;


    @Override
    public BookResponseDTO addBook(BookRequestDTO dto) {
        Author author = authorRepository.findById(dto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + dto.getAuthorId()));

        Book book = Book.builder()
                .isbn(dto.getIsbn())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .author(author)
                .price(dto.getPrice())
                .stockQuantity(dto.getStockQuantity())
                .category(dto.getCategory())
                .publisher(dto.getPublisher())
                .publishedYear(dto.getPublishedYear())
                .build();

        bookRepository.save(book);
        return mapToDTO(book);
    }

    @Override
    public BookResponseDTO updateBook(Long id, BookRequestDTO dto) {
        // Step 1: Get the existing book
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        // Step 2: Update fields if they are present in DTO
        if (dto.getTitle() != null && !dto.getTitle().isBlank()) book.setTitle(dto.getTitle());
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) book.setDescription(dto.getDescription());
        if (dto.getPrice() != null) book.setPrice(dto.getPrice());
        if (dto.getStockQuantity() != null) book.setStockQuantity(dto.getStockQuantity());
        if (dto.getCategory() != null && !dto.getCategory().isBlank()) book.setCategory(dto.getCategory());
        if (dto.getPublisher() != null && !dto.getPublisher().isBlank()) book.setPublisher(dto.getPublisher());
        if (dto.getPublishedYear() != null) book.setPublishedYear(dto.getPublishedYear());

        // 🔹 Optional: Update Author if provided
        if (dto.getAuthorId() != null) {
            Author newAuthor = authorRepository.findById(dto.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + dto.getAuthorId()));
            book.setAuthor(newAuthor);
        }

        // Step 3: Save and return updated DTO
        return mapToDTO(bookRepository.save(book));
    }


    @Override
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    @Override
    public List<BookResponseDTO> getAllBooks() {
        return bookRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BookResponseDTO getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToDTO(book);
    }

    @Override
    public BookResponseDTO updateStock(Long id, Integer newQuantity) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        book.setStockQuantity(newQuantity);
        bookRepository.save(book);
        return mapToDTO(book);
    }

    // 🔍 Combined search (Title, Author, ISBN)
    @Override
    public List<BookResponseDTO> searchBooks(String keyword) {
        List<Book> books = bookRepository.searchAcrossTitleAuthorIsbn(keyword);
        if (books.isEmpty()) {
            throw new ResourceNotFoundException("No books found matching: " + keyword);
        }
        return books.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // 🔁 Mapper method
    private BookResponseDTO mapToDTO(Book book) {
        return BookResponseDTO.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .description(book.getDescription())
                .authorName(book.getAuthor().getName())
                .price(book.getPrice())
                .stockQuantity(book.getStockQuantity())
                .category(book.getCategory())
                .publisher(book.getPublisher())
                .publishedYear(book.getPublishedYear())
                .build();
    }
}
