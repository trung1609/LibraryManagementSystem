package com.example.LibraryManagementSystem.payload.request;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RenewalRequest {

    @NotNull(message = "Book Loan Id cannot be null")
    private Long bookLoanId;

    @Min(value = 1, message = "Minimum renewal days is 1")
    private Integer extendDays = 14;

    private String notes;
}
