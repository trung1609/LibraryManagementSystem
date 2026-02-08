package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.domain.BookLoanStatus;
import com.example.LibraryManagementSystem.domain.BookLoanType;
import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.mapper.BookLoanMapper;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.BookLoan;
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
import com.example.LibraryManagementSystem.service.SubscriptionService;
import com.example.LibraryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
    public BookLoanDTO checkOutBook(CheckoutRequest request) throws SubscriptionException, BookException {
        Users users = userService.getCurrentUser();
        return checkoutBookForUsers(users.getId(), request);
    }

    @Override
    public BookLoanDTO checkoutBookForUsers(Long userId, CheckoutRequest request) throws SubscriptionException, BookException {
        // 1. Validate user exist
        Users users = userService.findById(userId);

        //2. Validate user has an active subscription
        SubscriptionDTO subscription = subscriptionService.getUsersActiveSubscriptions(users.getId());

        //3. Validate book exists and is available
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new BookException("Book not found with id: " + request.getBookId()));

        if (!book.getActive()) {
            throw new BookException("Book is not available");
        }

        if (book.getAvailableCopies() == 0) {
            throw new BookException("Book is out of stock");
        }

        //4. Check if the user already has this book checked out
        if (bookLoanRepository.hasActiveCheckout(userId, book.getId())) {
            throw new BookException("User already has this book checked out");
        }

        //5. Check a user's active checkout limit
        long activeCheckouts = bookLoanRepository.countActiveBookLoanByUser(userId);

        int maxBookAllowed = subscription.getMaxBooksAllowed();
        if (activeCheckouts >= maxBookAllowed) {
            throw new SubscriptionException("User has reached the checkout limit");
        }

        //6. Check for overdue books
        long overdueCount = bookLoanRepository.countOverdueBookLoansByUser(userId);
        if (overdueCount > 0) {
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
    public BookLoanDTO checkinBook(CheckinRequest request) throws BookException {
        //1. Validate book loan exists
        BookLoan bookLoan = bookLoanRepository.findById(request.getBookLoanId()).orElseThrow(() -> new RuntimeException("Book loan not found with id: " + request.getBookLoanId()));

        //2. Check if already returned
        if (!bookLoan.isActive()) {
            throw new BookException("Book loan is not active");
        }

        //3. set a return date
        bookLoan.setReturnDate(LocalDate.now());

        //4. set status
        BookLoanStatus condition = request.getCondition();
        if (condition == null) {
            bookLoan.setStatus(BookLoanStatus.RETURNED);
        }

        //5. fine todo
        bookLoan.setOverdueDays(0);
        bookLoan.setIsOverdue(false);
        //6.
        bookLoan.setNotes("Book returned by user");

        //7. update book availability
        if (condition != BookLoanStatus.LOST) {
            Book book = bookLoan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);

            // process next reservation todo

        }

        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public BookLoanDTO renewCheckout(RenewalRequest request) {
        //1. Validate book loan exists
        BookLoan bookLoan = bookLoanRepository.findById(request.getBookLoanId()).orElseThrow(() -> new RuntimeException("Book loan not found with id: " + request.getBookLoanId()));

        //2. Check if can be renewed
        if (!bookLoan.canRenew()) {
            throw new RuntimeException("Book loan cannot be renewed");
        }
        //3. Update due date
        bookLoan.setDueDate(bookLoan.getDueDate().plusDays(request.getExtendDays()));

        //4. Update renewal count
        bookLoan.setRenewalCount(bookLoan.getRenewalCount() + 1);

        bookLoan.setNotes("Book renewed by user");

        BookLoan savedBookLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDTO(savedBookLoan);
    }

    @Override
    public PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int size) {
        Users users = userService.getCurrentUser();
        Page<BookLoan> bookLoanPage;

        if (status != null) {
            Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
            bookLoanPage = bookLoanRepository.findByStatusAndUsers(status, users, pageable);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            bookLoanPage = bookLoanRepository.findByUsersId(users.getId(), pageable);
        }
        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public PageResponse<BookLoanDTO> getBookLoans(BookLoanSearchRequest request) {
        Pageable pageable = createPageable(
                request.getPage(),
                request.getPageSize(),
                request.getSortBy(),
                request.getSortDirection()
        );
        Page<BookLoan> bookLoanPage;

        if (Boolean.TRUE.equals(request.getOverdueOnly())){
            bookLoanPage = bookLoanRepository.findOverdueBookLoans(LocalDate.now(), pageable);
        }else if (request.getUserId() != null){
            bookLoanPage = bookLoanRepository.findByUsersId(request.getUserId(), pageable);
        } else if (request.getBookId() != null) {
            bookLoanPage = bookLoanRepository.findByBookId(request.getBookId(), pageable);
        } else if (request.getStatus() != null){
            bookLoanPage = bookLoanRepository.findByStatus(request.getStatus(), pageable);
        } else if (request.getStartDate() != null && request.getEndDate() != null) {
            bookLoanPage = bookLoanRepository.findBookLoansByDateRange(
                    request.getStartDate(),
                    request.getEndDate(),
                    pageable);
        }else {
            bookLoanPage = bookLoanRepository.findAll(pageable);
        }

        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public int updateOverdueBookLoan() {
        return 0;
    }

    private Pageable createPageable(int page, int size, String sortBy, String sortDirection) {
        size = Math.min(size, 100);
        size = Math.max(size, 1);

        Sort sort = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        return PageRequest.of(page, size, sort);
    }

    private PageResponse<BookLoanDTO> convertToPageResponse(Page<BookLoan> bookLoanDTOPage) {
        List<BookLoanDTO> bookLoanDTOS = bookLoanDTOPage.getContent()
                .stream()
                .map(bookLoanMapper::toDTO)
                .toList();

        return new PageResponse<>(
                bookLoanDTOS,
                bookLoanDTOPage.getNumber(),
                bookLoanDTOPage.getSize(),
                bookLoanDTOPage.getTotalElements(),
                bookLoanDTOPage.getTotalPages(),
                bookLoanDTOPage.isLast(),
                bookLoanDTOPage.isFirst(),
                bookLoanDTOPage.isEmpty()
        );
    }
}
