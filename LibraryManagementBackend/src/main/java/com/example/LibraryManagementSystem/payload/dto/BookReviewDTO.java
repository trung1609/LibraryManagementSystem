package com.example.LibraryManagementSystem.payload.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReviewDTO {
    private Long id;

    @NotNull(message = "User ID cannot be null")
    private Long userId;

    private String userName;

    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    private String bookTitle;

    @NotNull(message = "Rating is mandatory")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must not exceed 5")
    private Integer rating;

    @NotBlank(message = "Review text cannot be blank")
    @Size(min = 10, max = 2000, message = "Review text must be between 10 and 2000 characters")
    private String reviewText;

    @Size(max = 200, message = "Title must not exceed 200 characters")
    private String title;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
}
