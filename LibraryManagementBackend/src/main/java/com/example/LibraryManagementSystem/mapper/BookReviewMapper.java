package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.model.BookReview;
import com.example.LibraryManagementSystem.payload.dto.BookReviewDTO;
import org.springframework.stereotype.Component;

@Component
public class BookReviewMapper {
    public BookReviewDTO toDTO(BookReview bookReview){
        if (bookReview == null) {
            return null;
        }

        BookReviewDTO dto = new BookReviewDTO();
        dto.setId(bookReview.getId());

        if (bookReview.getUsers() != null){
            dto.setUserId(bookReview.getUsers().getId());
            dto.setUserName(bookReview.getUsers().getFullName());
        }

        if (bookReview.getBook() != null){
            dto.setBookId(bookReview.getBook().getId());
            dto.setBookTitle(bookReview.getBook().getTitle());
        }

        dto.setRating(bookReview.getRating());
        dto.setReviewText(bookReview.getReviewText());
        dto.setTitle(bookReview.getTitle());
        dto.setCreatedAt(bookReview.getCreatedAt());
        dto.setUpdatedAt(bookReview.getUpdatedAt());
        return dto;
    }
}
