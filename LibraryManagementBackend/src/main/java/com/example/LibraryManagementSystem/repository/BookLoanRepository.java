package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.domain.BookLoanStatus;
import com.example.LibraryManagementSystem.model.BookLoan;
import com.example.LibraryManagementSystem.model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookLoanRepository extends JpaRepository<BookLoan, Long> {

    Page<BookLoan> findByUsersId(Long userId, Pageable pageable);
    Page<BookLoan> findByStatusAndUsers(BookLoanStatus status, Users users, Pageable pageable);
    Page<BookLoan> findByStatus(BookLoanStatus status, Pageable pageable);
    Page<BookLoan> findByBookId(Long bookId, Pageable pageable);
    @Query("select case when count(bl) > 0 " +
            "then true " +
            "else false end " +
            "from BookLoan bl " +
            "where bl.users.id = :userId " +
            "and bl.book.id = :bookId " +
            "and (bl.status = 'CHECKED_OUT' or bl.status = 'OVERDUE')")
    boolean hasActiveCheckout(@Param("userId") Long userId,
                              @Param("bookId") long bookId);


    @Query("select count(bl) from BookLoan bl where bl.users.id = :userId " +
            "and (bl.status = 'CHECKED_OUT' or bl.status = 'OVERDUE')")
    long countActiveBookLoanByUser(@Param("userId") Long userId);

    @Query("select count(bl) from BookLoan bl where bl.users.id = :userId and bl.status = 'OVERDUE'")
    long countOverdueBookLoansByUser(Long userId);

    @Query("select bl from BookLoan bl where bl.dueDate < :currentDate " +
            "and (bl.status = 'CHECKED_OUT' or bl.status = 'OVERDUE')")
    Page<BookLoan> findOverdueBookLoans(@Param("currentDate") LocalDate currentDate, Pageable pageable);

    @Query("select bl from BookLoan bl where bl.checkoutDate between :startDate and :endDate")
    Page<BookLoan> findBookLoansByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, Pageable pageable);

    boolean existsByUserIdAndBookIdAndStatus(Long userId, Long bookId, BookLoanStatus status);
}
