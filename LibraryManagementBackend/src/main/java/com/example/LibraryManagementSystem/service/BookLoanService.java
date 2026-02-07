package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.domain.BookLoanStatus;
import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.payload.dto.BookLoanDTO;
import com.example.LibraryManagementSystem.payload.request.BookLoanSearchRequest;
import com.example.LibraryManagementSystem.payload.request.CheckinRequest;
import com.example.LibraryManagementSystem.payload.request.CheckoutRequest;
import com.example.LibraryManagementSystem.payload.request.RenewalRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;

public interface BookLoanService {
    BookLoanDTO checkOutBook(CheckoutRequest request);

    BookLoanDTO checkoutBookForUsers(Long userId, CheckoutRequest request) throws SubscriptionException, BookException;

    BookLoanDTO checkinBook(CheckinRequest request);

    BookLoanDTO renewCheckout(RenewalRequest request);

    PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int size);

    PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request);

    int updateOverdueBookLoan();


}
