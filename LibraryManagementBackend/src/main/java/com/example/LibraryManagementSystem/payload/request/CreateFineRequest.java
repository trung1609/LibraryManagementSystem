package com.example.LibraryManagementSystem.payload.request;

import com.example.LibraryManagementSystem.domain.FineType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateFineRequest {

    @NotNull(message = "Book loan id cannot be null")
    private Long bookLoanId;

    @NotNull(message = "Fine type cannot be null")
    private FineType type;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Long amount;

    private String reason;

    private String notes;
}
