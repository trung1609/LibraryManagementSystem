package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.mapper.BookMapper;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.payload.dto.BookDTO;
import com.example.LibraryManagementSystem.payload.request.BookSearchRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BookRepository bookRepository;

    @Override
    public BookDTO createBook(BookDTO bookDTO) throws BookException {
        if (bookRepository.existsByIsbn(bookDTO.getIsbn())) {
            throw new BookException("Book with ISBN " + bookDTO.getIsbn() + " already exists.");
        }
        Book book = bookMapper.toEntity(bookDTO);

        book.isAvailableCopiesValid();
        Book savedBook = bookRepository.save(book);

        return bookMapper.toDTO(savedBook);
    }

    @Override
    public BookDTO getBookById(Long id) throws BookException {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new BookException("Book not found with id: " + id)
        );
        return bookMapper.toDTO(book);
    }

    @Override
    public List<BookDTO> createBooksBulk(List<BookDTO> bookDTOs) throws BookException {
        List<BookDTO> createdBooks = new ArrayList<>();
        for (BookDTO bookDTO : bookDTOs) {
            BookDTO dto = createBook(bookDTO);
            createdBooks.add(dto);
        }
        return bookDTOs;
    }

    @Override
    public BookDTO getBooksByISBN(String ISBN) throws BookException {
        Book book = bookRepository.findByIsbn(ISBN).orElseThrow(
                () -> new BookException("Book not found with id: " + ISBN)
        );
        return bookMapper.toDTO(book);
    }

    @Override
    public BookDTO updateBook(Long bookId, BookDTO bookDTO) throws BookException {
        Book existingBook = bookRepository.findById(bookId).orElseThrow(
                () -> new BookException("Book not found with id: " + bookId
                ));
        bookMapper.updateEntityFromDTO(bookDTO, existingBook);
        existingBook.isAvailableCopiesValid();
        Book savedBook = bookRepository.save(existingBook);
        return bookMapper.toDTO(savedBook);
    }

    @Override
    public void deleteBook(Long id) {
        Book existingBook = bookRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Book not found with id: " + id
                ));
        existingBook.setActive(false);
        bookRepository.save(existingBook);
    }

    @Override
    public void hardDeleteBook(Long bookId) {
        Book existingBook = bookRepository.findById(bookId).orElseThrow(
                () -> new RuntimeException("Book not found with id: " + bookId
                ));
        bookRepository.delete(existingBook);
    }

    @Override
    public PageResponse<BookDTO> searchBooksWithFilters(BookSearchRequest searchRequest) {
        Pageable pageable = createPageable(searchRequest.getPage(),
                searchRequest.getPageSize(),
                searchRequest.getSortBy(),
                searchRequest.getSortDirection());
        Page<Book> bookPage = bookRepository.searchBooksWithFiltersNative(
                searchRequest.getSearchTerm(),
                searchRequest.getGenreId(),
                searchRequest.getAvailableOnly(),
                pageable
        );
        return convertToPageResponse(bookPage);
    }

    @Override
    public long getTotalActiveBooks() {
        return bookRepository.countByActiveTrue();
    }

    @Override
    public long getTotalAvailableBooks() {
        return bookRepository.countAvailableBooks();
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        size = Math.min(size, 10);
        size = Math.max(size, 1);

        // Convert Java property names to database column names for native queries
        String dbColumnName = convertToDbColumnName(sortBy);

        Sort sort = sortDirection.equalsIgnoreCase("asc")
                ? Sort.by(dbColumnName).ascending() : Sort.by(dbColumnName).descending();
        return PageRequest.of(page, size, sort);

    }

    private String convertToDbColumnName(String javaPropertyName) {
        // Convert camelCase to snake_case for database columns
        return javaPropertyName.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private PageResponse<BookDTO> convertToPageResponse(Page<Book> books) {
        List<BookDTO> bookDTOS = books.getContent()
                .stream()
                .map(bookMapper::toDTO)
                .toList();
        return new PageResponse<>(bookDTOS,books.getNumber(),
                books.getSize(),
                books.getTotalElements(),
                books.getTotalPages(),
                books.isLast(),
                books.isFirst(),
                books.isEmpty());
    }
}
