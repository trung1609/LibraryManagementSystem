package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.domain.BookLoanStatus;
import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.payload.dto.BookLoanDTO;
import com.example.LibraryManagementSystem.payload.request.BookLoanSearchRequest;
import com.example.LibraryManagementSystem.payload.request.CheckinRequest;
import com.example.LibraryManagementSystem.payload.request.CheckoutRequest;
import com.example.LibraryManagementSystem.payload.request.RenewalRequest;
import com.example.LibraryManagementSystem.payload.response.ApiResponse;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.service.BookLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book-loans")
@RequiredArgsConstructor
public class BookLoanController {
    private final BookLoanService bookLoanService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutBook(@Valid @RequestBody CheckoutRequest request) throws SubscriptionException, BookException {
        BookLoanDTO dto = bookLoanService.checkOutBook(request);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/checkout/user/{userId}")
    public ResponseEntity<?> checkoutBookForUser(@PathVariable Long userId,
                                                 @Valid @RequestBody CheckoutRequest request) throws SubscriptionException, BookException {
        BookLoanDTO dto = bookLoanService.checkoutBookForUsers(userId, request);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checkin(@Valid @RequestBody CheckinRequest request) throws BookException {
        BookLoanDTO dto = bookLoanService.checkinBook(request);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/renew")
    public ResponseEntity<?> renew(
            @Valid @RequestBody RenewalRequest request
    ) throws BookException {
        BookLoanDTO dto = bookLoanService.renewCheckout(request);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyBookLoans(
            @RequestParam(required = false)BookLoanStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
            ){
        PageResponse<BookLoanDTO> response = bookLoanService.getMyBookLoans(status, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public ResponseEntity<?> getAllBookLoans(
            @RequestBody BookLoanSearchRequest request
    ){
        PageResponse<BookLoanDTO> bookLoanPage = bookLoanService.getBookLoans(request);
        return ResponseEntity.ok(bookLoanPage);
    }

    @PostMapping("/admin/update-overdue")
    public ResponseEntity<?> updateOverdueBookLoans(){

        int updatedCount = bookLoanService.updateOverdueBookLoan();
        return ResponseEntity.ok(new ApiResponse("Overdue book loans updated successfully", true));
    }
}
