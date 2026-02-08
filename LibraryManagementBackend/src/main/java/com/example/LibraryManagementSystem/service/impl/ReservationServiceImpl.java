package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.domain.BookLoanStatus;
import com.example.LibraryManagementSystem.domain.ReservationStatus;
import com.example.LibraryManagementSystem.mapper.ReservationMapper;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.Reservation;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.ReservationDTO;
import com.example.LibraryManagementSystem.payload.request.ReservationRequest;
import com.example.LibraryManagementSystem.payload.request.ReservationSearchRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.repository.BookLoanRepository;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.ReservationRepository;
import com.example.LibraryManagementSystem.service.ReservationService;
import com.example.LibraryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final ReservationMapper reservationMapper;

    int MAX_RESERVATION_LIMIT = 5;

    @Override
    public ReservationDTO createReservation(ReservationRequest request) {
        return null;
    }

    @Override
    public ReservationDTO createReservationForUsers(Long userId, ReservationRequest request) {
        boolean alreadyHasLoan = bookLoanRepository.existsByUserIdAndBookIdAndStatus(userId, request.getBookId(), BookLoanStatus.CHECKED_OUT);

        if (alreadyHasLoan) {
            throw new RuntimeException("User already has a loan on this book");
        }

        //1. Validate user exist
        Users users = userService.getCurrentUser();

        //2. Validate book exists
        Book book = bookRepository.findById(request.getBookId()).orElseThrow(() -> new RuntimeException("Book not found with id: " + request.getBookId()));

        //3.
        if (reservationRepository.hasActiveReservation(userId, book.getId())) {
            throw new RuntimeException("User already has an active reservation on this book");
        }

        //4. check if book is already available
        if (book.getAvailableCopies() > 0) {
            throw new RuntimeException("Book is already available");
        }

        //5. check user's active reservation limit
        long activeReservation = reservationRepository.countActiveReservationsByUser(userId);
        if (activeReservation >= MAX_RESERVATION_LIMIT) {
            throw new RuntimeException("User has reached the reservation limit");
        }

        //6. create reservation
        Reservation reservation = new Reservation();
        reservation.setUsers(users);
        reservation.setBook(book);
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setReservedAt(LocalDateTime.now());
        reservation.setNotificationSent(false);
        reservation.setNotes(request.getNotes());

        long pendingCount = reservationRepository.countPendingReservationByBook(book.getId());
        reservation.setQueuePosition((int) pendingCount + 1);
        Reservation savedReservation = reservationRepository.save(reservation);
        return reservationMapper.toDto(savedReservation);
    }

    @Override
    public ReservationDTO cancelReservation(Long reservationId) {
        return null;
    }

    @Override
    public ReservationDTO fulfillReservation(Long reservationId) {
        return null;
    }

    @Override
    public PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest request) {
        return null;
    }

    @Override
    public PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest request) {
        return null;
    }
}
