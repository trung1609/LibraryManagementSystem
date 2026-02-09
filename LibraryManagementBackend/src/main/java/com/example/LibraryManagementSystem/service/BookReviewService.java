package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.payload.dto.BookReviewDTO;
import com.example.LibraryManagementSystem.payload.request.CreateReviewRequest;
import com.example.LibraryManagementSystem.payload.request.UpdateReviewRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;

public interface BookReviewService {
    BookReviewDTO createReview(CreateReviewRequest request);

    BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest request);

    void deleteReview(Long reviewId);

    PageResponse<BookReviewDTO> getReviewsByBookId(Long bookId, int page, int size);
}
