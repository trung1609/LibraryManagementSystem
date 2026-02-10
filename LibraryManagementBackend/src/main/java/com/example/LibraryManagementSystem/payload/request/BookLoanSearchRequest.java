package com.example.LibraryManagementSystem.payload.request;

import com.example.LibraryManagementSystem.domain.BookLoanStatus;
import com.example.LibraryManagementSystem.model.BookLoan;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookLoanSearchRequest {
    private Long userId;
    private Long bookId;
    private BookLoanStatus status;
    private Boolean overdueOnly;
    private Boolean unpaidFinesOnly;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer page = 0;
    private Integer pageSize = 20;
    private String sortBy = "createdAt";
    private String sortDirection = "desc";
}
