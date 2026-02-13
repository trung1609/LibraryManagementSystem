package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.payload.dto.BookDTO;
import com.example.LibraryManagementSystem.payload.response.ApiResponse;
import com.example.LibraryManagementSystem.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/books")
public class AdminBookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) throws BookException {
        BookDTO createdBook = bookService.createBook(bookDTO);
        return ResponseEntity.ok(createdBook);
    }

    @PostMapping("bulk")
    public ResponseEntity<List<BookDTO>> createBookBulk(
            @Valid @RequestBody List<BookDTO> bookDTOS) throws BookException {
        List<BookDTO> createdBooks = bookService.createBooksBulk(bookDTOS);
        return ResponseEntity.ok(createdBooks);
    }


    @PutMapping("{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @Valid @RequestBody BookDTO bookDTO) throws BookException {
        BookDTO updateBook = bookService.updateBook(id, bookDTO);
        return ResponseEntity.ok(updateBook);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse> softDeleteBook(@PathVariable Long id) throws BookException {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book soft deleted successfully", true));

    }

    @DeleteMapping("{id}/permanent")
    public ResponseEntity<ApiResponse> hardDeleteBook(@PathVariable Long id) throws BookException {
        bookService.hardDeleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book hard deleted successfully", true));
    }
}
