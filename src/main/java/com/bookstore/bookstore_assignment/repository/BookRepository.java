package com.bookstore.bookstore_assignment.repository;

import com.bookstore.bookstore_assignment.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByIsbn(String isbn);
    List<Book> findByTitleContainingIgnoreCase(String title);

    @Query("""
    SELECT b FROM Book b 
    WHERE LOWER(b.title) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(b.author.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
       OR LOWER(b.isbn) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
    List<Book> searchAcrossTitleAuthorIsbn(String keyword);

}