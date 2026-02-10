package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.BookReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookReviewRepository extends JpaRepository<BookReview, Long> {
    Page<BookReview> findByBook(Book book, Pageable pageable);
    boolean existsByUsersIdAndBookId(Long userId, Long bookId);
}
