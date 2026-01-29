package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.payload.dto.BookDTO;
import com.example.LibraryManagementSystem.payload.request.BookSearchRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;

import java.util.List;

public interface BookService {
    BookDTO createBook(BookDTO bookDTO) throws BookException;

    BookDTO getBookById(Long id) throws BookException;

    List<BookDTO> createBooksBulk(List<BookDTO> bookDTOs) throws BookException;

    BookDTO getBooksByISBN(String ISBN) throws BookException;

    BookDTO updateBook(Long bookId, BookDTO bookDTO) throws BookException;

    void deleteBook(Long id);

    void hardDeleteBook(Long bookId);

    PageResponse<BookDTO> searchBooksWithFilters(
            BookSearchRequest searchRequest
    );

    long getTotalActiveBooks();

    long getTotalAvailableBooks();
}
