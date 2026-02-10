package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.payload.request.CreateReviewRequest;
import com.example.LibraryManagementSystem.payload.request.UpdateReviewRequest;
import com.example.LibraryManagementSystem.payload.response.ApiResponse;
import com.example.LibraryManagementSystem.service.BookReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/book-reviews")
@RequiredArgsConstructor
public class BookReviewController {
    private final BookReviewService bookReviewService;

    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequest request) {
        return ResponseEntity.ok(bookReviewService.createReview(request));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<?> updateReview(@PathVariable Long reviewId, @RequestBody UpdateReviewRequest request) {
        return ResponseEntity.ok(bookReviewService.updateReview(reviewId, request));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        bookReviewService.deleteReview(reviewId);
        return ResponseEntity.ok(new ApiResponse("Review deleted successfully", true));
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getReviewByBookId(
            @PathVariable Long bookId
            , @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(bookReviewService.getReviewsByBookId(bookId, page, size));
    }
}
