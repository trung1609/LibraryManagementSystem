package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.domain.BookLoanStatus;
import com.example.LibraryManagementSystem.domain.BookLoanType;
import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.mapper.BookLoanMapper;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.BookLoan;
import com.example.LibraryManagementSystem.model.Subscription;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.BookLoanDTO;
import com.example.LibraryManagementSystem.payload.dto.SubscriptionDTO;
import com.example.LibraryManagementSystem.payload.request.BookLoanSearchRequest;
import com.example.LibraryManagementSystem.payload.request.CheckinRequest;
import com.example.LibraryManagementSystem.payload.request.CheckoutRequest;
import com.example.LibraryManagementSystem.payload.request.RenewalRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.repository.BookLoanRepository;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.service.BookLoanService;
import com.example.LibraryManagementSystem.service.BookService;
import com.example.LibraryManagementSystem.service.SubscriptionService;
import com.example.LibraryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookLoanServiceImpl implements BookLoanService {

    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final BookRepository bookRepository;
    private final BookLoanMapper bookLoanMapper;

    @Override
    public BookLoanDTO checkOutBook(CheckoutRequest request) {

        return null;
    }

    @Override
    public BookLoanDTO checkoutBookForUsers(Long userId, CheckoutRequest request) throws SubscriptionException, BookException {
        // 1. Validate user exist
        Users users = userService.findById(userId);

        //2. Validate user has an active subscription
        SubscriptionDTO subscription = subscriptionService.getUsersActiveSubscriptions(users.getId());

        //3. Validate book exists and is available
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new BookException("Book not found with id: " + request.getBookId()));

        if (!book.getActive()){
            throw new BookException("Book is not available");
        }

        if (book.getAvailableCopies() == 0){
            throw new BookException("Book is out of stock");
        }

        //4. Check if the user already has this book checked out
        if (bookLoanRepository.hasActiveCheckout(userId, book.getId())){
            throw new BookException("User already has this book checked out");
        }

        //5. Check a user's active checkout limit
        long activeCheckouts = bookLoanRepository.countActiveBookLoanByUser(userId);

        int maxBookAllowed = subscription.getMaxBooksAllowed();
        if (activeCheckouts >= maxBookAllowed){
            throw new SubscriptionException("User has reached the checkout limit");
        }

        //6. Check for overdue books
        long overdueCount = bookLoanRepository.countOverdueBookLoansByUser(userId);
        if (overdueCount > 0){
            throw new SubscriptionException("User has overdue books");
        }

        //7. Create a book loan
        BookLoan bookLoan = BookLoan.builder()
                .users(users)
                .book(book)
                .type(BookLoanType.CHECKOUT)
                .status(BookLoanStatus.CHECKED_OUT)
                .checkoutDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(request.getCheckoutDays()))
                .renewalCount(0)
                .maxRenewals(2)
                .notes(request.getNotes())
                .overdueDays(0)
                .build();

        //9. Update book-available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        //10. Save book loan
        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public BookLoanDTO checkinBook(CheckinRequest request) {
        return null;
    }

    @Override
    public BookLoanDTO renewCheckout(RenewalRequest request) {
        return null;
    }

    @Override
    public PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int size) {
        return null;
    }

    @Override
    public PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request) {
        return null;
    }

    @Override
    public int updateOverdueBookLoan() {
        return 0;
    }
}
