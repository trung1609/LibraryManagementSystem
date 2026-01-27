package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.payload.dto.BookDTO;
import com.example.LibraryManagementSystem.payload.request.BookSearchRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private BookRepository bookRepository;

    @Override
    public BookDTO createBook(BookDTO bookDTO) {
        return null;
    }

    @Override
    public BookDTO getBookById(Long id) {
        return null;
    }

    @Override
    public List<BookDTO> createBooksBulk() {
        return List.of();
    }

    @Override
    public BookDTO getBooksByISBN(String ISBN) {
        return null;
    }

    @Override
    public BookDTO updateBook(Long bookId, BookDTO bookDTO) {
        return null;
    }

    @Override
    public void deleteBook(Long id) {

    }

    @Override
    public void hardDeleteBook(BookDTO bookDTO) {

    }

    @Override
    public PageResponse<BookDTO> searchBooksWithFilters(BookSearchRequest searchRequest) {
        return null;
    }

    @Override
    public long getTotalActiveBooks() {
        return 0;
    }

    @Override
    public long getTotalAvailableBooks() {
        return 0;
    }
}
