package com.example.LibraryManagementSystem.payload.dto;

import com.example.LibraryManagementSystem.domain.FineStatus;
import com.example.LibraryManagementSystem.domain.FineType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FineDTO {
    private Long id;

    @NotNull(message = "User ID cannot be null")
    private Long userId;
    private String userName;
    private String userEmail;

    @NotNull(message = "Book Loan ID cannot be null")
    private Long bookLoanId;
    private String bookTitle;
    private String bookIsbn;

    @NotNull(message = "Fine type is mandatory")
    private FineType type;

    @NotNull(message = "Amount is mandatory")
    @PositiveOrZero(message = "Amount must be positive")
    private Long amount;

    @PositiveOrZero(message = "Amount paid must be positive")
    private Long amountPaid;

    private Long amountOutstanding;

    @NotNull(message = "Status cannot be null")
    private FineStatus status;

    private String reason;

    private String notes;

    private Long waivedByUserId;

    private String waivedByUserName;

    private LocalDateTime waivedAt;

    private String waiverReason;

    private LocalDateTime paidAt;

    private Long processedByUserId;

    private String processedByUserName;

    private String transactionId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
