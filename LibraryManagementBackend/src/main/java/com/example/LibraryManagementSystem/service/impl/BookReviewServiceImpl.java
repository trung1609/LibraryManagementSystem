package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.domain.BookLoanStatus;
import com.example.LibraryManagementSystem.mapper.BookReviewMapper;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.BookLoan;
import com.example.LibraryManagementSystem.model.BookReview;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.BookReviewDTO;
import com.example.LibraryManagementSystem.payload.request.CreateReviewRequest;
import com.example.LibraryManagementSystem.payload.request.UpdateReviewRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.repository.BookLoanRepository;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.BookReviewRepository;
import com.example.LibraryManagementSystem.service.BookReviewService;
import com.example.LibraryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookReviewServiceImpl implements BookReviewService {
    private final BookReviewRepository bookReviewRepository;
    private final BookReviewMapper bookReviewMapper;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final BookLoanRepository bookLoanRepository;

    @Override
    public BookReviewDTO createReview(CreateReviewRequest request) {
        //1. fetch the logged user
        Users users = userService.getCurrentUser();

        //2. validate book exists
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new RuntimeException("Book not found with id: " + request.getBookId()));

        //3. check if the user has already reviewed this book
        if (bookReviewRepository.existsByUsersIdAndBookId(users.getId(), book.getId())) {
            throw new RuntimeException("User has already reviewed this book");
        }
        
        //4. check if the user has read the book
        boolean hasReadBook = hasUserReadBook(users.getId(), book.getId());
        if (!hasReadBook) {
            throw new RuntimeException("User has not read the book");
        }

        //5. create a review
        BookReview bookReview = new BookReview();
        bookReview.setUsers(users);
        bookReview.setBook(book);
        bookReview.setRating(request.getRating());
        bookReview.setReviewText(request.getReviewText());
        bookReview.setTitle(request.getTitle());

        BookReview savedReview = bookReviewRepository.save(bookReview);
        return bookReviewMapper.toDTO(savedReview);
    }

    @Override
    public BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request) {
        //1. fetch the logged user
        Users users = userService.getCurrentUser();

        //2. find the review to update
        BookReview review = bookReviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));

        if (!review.getUsers().getId().equals(users.getId())) {
            throw new RuntimeException("You are not authorized to update this review");
        }

        //3. update the review
        review.setRating(request.getRating());
        review.setReviewText(request.getReviewText());
        review.setTitle(request.getTitle());
        BookReview updatedReview = bookReviewRepository.save(review);
        return bookReviewMapper.toDTO(updatedReview);
    }

    @Override
    public void deleteReview(Long reviewId) {
        Users users = userService.getCurrentUser();

        BookReview review = bookReviewRepository.findById(reviewId).orElseThrow(() -> new RuntimeException("Review not found with id: " + reviewId));
        if (!review.getUsers().getId().equals(users.getId())) {
            throw new RuntimeException("You are not authorized to delete this review");
        }

        bookReviewRepository.delete(review);
    }

    @Override
    public PageResponse<BookReviewDTO> getReviewsByBookId(Long bookId, int page, int size) {

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found with id: " + bookId));
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());

        Page<BookReview> bookReviewPage = bookReviewRepository.findByBook(book, pageable);

        return convertToPageResponse(bookReviewPage);
    }

    private boolean hasUserReadBook(Long userId, long bookId) {
        List<BookLoan> bookLoans = bookLoanRepository.findByBookId(bookId);
        return bookLoans.stream()
                .anyMatch(l->l.getUsers().getId().equals(userId) &&
                        l.getBook().getId() == bookId &&
                        l.getStatus() == BookLoanStatus.RETURNED);
    }

    private PageResponse<BookReviewDTO> convertToPageResponse(Page<BookReview> bookReviewPage) {
        List<BookReviewDTO> bookReviewDTOS = bookReviewPage.getContent()
                .stream()
                .map(bookReviewMapper::toDTO)
                .toList();

        return new PageResponse<>(
                bookReviewDTOS,
                bookReviewPage.getNumber(),
                bookReviewPage.getSize(),
                bookReviewPage.getTotalElements(),
                bookReviewPage.getTotalPages(),
                bookReviewPage.isLast(),
                bookReviewPage.isFirst(),
                bookReviewPage.isEmpty());
    }
}
