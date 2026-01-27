package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.payload.dto.BookDTO;
import com.example.LibraryManagementSystem.payload.request.BookSearchRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;

import java.util.List;

public interface BookService {
    BookDTO createBook(BookDTO bookDTO);

    BookDTO getBookById(Long id);

    List<BookDTO> createBooksBulk();

    BookDTO getBooksByISBN(String ISBN);

    BookDTO updateBook(Long bookId, BookDTO bookDTO);

    void deleteBook(Long id);

    void hardDeleteBook(BookDTO bookDTO);
    PageResponse<BookDTO> searchBooksWithFilters(
            BookSearchRequest searchRequest
    );

    long getTotalActiveBooks();

    long getTotalAvailableBooks();
}
