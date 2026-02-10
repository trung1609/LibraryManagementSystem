package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.model.BookLoan;
import com.example.LibraryManagementSystem.payload.dto.BookLoanDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class BookLoanMapper {

    public BookLoanDTO toDTO(BookLoan bookLoan) {
        if (bookLoan == null) {
            return null;
        }

        BookLoanDTO dto = new BookLoanDTO();
        dto.setId(bookLoan.getId());

        if (bookLoan.getUsers() != null){
            dto.setUserId(bookLoan.getUsers().getId());
            dto.setUserName(bookLoan.getUsers().getFullName());
            dto.setUserEmail(bookLoan.getUsers().getEmail());
        }

        if (bookLoan.getBook() != null){
            dto.setBookId(bookLoan.getBook().getId());
            dto.setBookTitle(bookLoan.getBook().getTitle());
            dto.setBookIsbn(bookLoan.getBook().getIsbn());
            dto.setBookAuthor(bookLoan.getBook().getAuthor());
            dto.setBookCoverImage(bookLoan.getBook().getCoverImageUrl());
        }

        dto.setType(bookLoan.getType());
        dto.setStatus(bookLoan.getStatus());
        dto.setCheckoutDate(bookLoan.getCheckoutDate());
        dto.setDueDate(bookLoan.getDueDate());
        dto.setRemainingDays(
                ChronoUnit.DAYS.between(LocalDate.now(), bookLoan.getDueDate())
        );
        dto.setReturnDate(bookLoan.getReturnDate());
        dto.setRenewalCount(bookLoan.getRenewalCount());
        dto.setMaxRenewals(bookLoan.getMaxRenewals());
        dto.setOverdueDays(bookLoan.getOverdueDays());
        dto.setNotes(bookLoan.getNotes());
        dto.setIsOverdue(bookLoan.getIsOverdue());
        dto.setCreatedAt(bookLoan.getCreatedAt());
        dto.setUpdatedAt(bookLoan.getUpdatedAt());
        return dto;
    }
}
