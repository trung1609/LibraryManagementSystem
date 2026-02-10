package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.domain.BookLoanStatus;
import com.example.LibraryManagementSystem.domain.ReservationStatus;
import com.example.LibraryManagementSystem.domain.UserRole;
import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.mapper.ReservationMapper;
import com.example.LibraryManagementSystem.model.Book;
import com.example.LibraryManagementSystem.model.Reservation;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.ReservationDTO;
import com.example.LibraryManagementSystem.payload.request.CheckoutRequest;
import com.example.LibraryManagementSystem.payload.request.ReservationRequest;
import com.example.LibraryManagementSystem.payload.request.ReservationSearchRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.repository.BookLoanRepository;
import com.example.LibraryManagementSystem.repository.BookRepository;
import com.example.LibraryManagementSystem.repository.ReservationRepository;
import com.example.LibraryManagementSystem.service.BookLoanService;
import com.example.LibraryManagementSystem.service.ReservationService;
import com.example.LibraryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final BookLoanRepository bookLoanRepository;
    private final UserService userService;
    private final BookRepository bookRepository;
    private final ReservationMapper reservationMapper;
    private final BookLoanService bookLoanService;

    int MAX_RESERVATION_LIMIT = 5;

    @Override
    public ReservationDTO createReservation(ReservationRequest request) {
        Users users = userService.getCurrentUser();
        return createReservationForUsers(users.getId(), request);
    }

    @Override
    public ReservationDTO createReservationForUsers(Long userId, ReservationRequest request) {
        boolean alreadyHasLoan = bookLoanRepository.existsByUsersIdAndBookIdAndStatus(userId, request.getBookId(), BookLoanStatus.CHECKED_OUT);

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

        //4. check if a book is already available
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
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));

        Users users = userService.getCurrentUser();
        if (!reservation.getUsers().getId().equals(users.getId()) && users.getRole() != UserRole.ROLE_ADMIN) {
            throw new RuntimeException("You are not authorized to cancel this reservation");
        }

        if (!reservation.canBeCancelled()){
            throw new RuntimeException("Reservation cannot be cancelled");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setCancelledAt(LocalDateTime.now());
        Reservation savedReservation = reservationRepository.save(reservation);

        return reservationMapper.toDto(savedReservation);
    }

    @Override
    public ReservationDTO fulfillReservation(Long reservationId) throws SubscriptionException, BookException {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow(() -> new RuntimeException("Reservation not found with id: " + reservationId));

        if (reservation.getBook().getAvailableCopies() <= 0) {
            throw new RuntimeException("Reservation cannot be fulfilled as book is not available");
        }

        reservation.setStatus(ReservationStatus.FULFILLED);
        reservation.setFulfilledAt(LocalDateTime.now());

        Reservation savedReservation = reservationRepository.save(reservation);

        CheckoutRequest request = new CheckoutRequest();
        request.setBookId(reservation.getBook().getId());
        request.setNotes("Assign Booked by Admin");

        bookLoanService.checkoutBookForUsers(reservation.getUsers().getId(), request);
        return reservationMapper.toDto(savedReservation);
    }

    @Override
    public PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest request) {
        Pageable pageable = createPageable(request);

        Page<Reservation> reservationPage = reservationRepository.searchReservationsWithFilters(
                request.getUserId(),
                request.getBookId(),
                request.getStatus(),
                request.getActiveOnly() != null ? request.getActiveOnly() : false,
                pageable
        );

        return buildPageResponse(reservationPage);
    }

    @Override
    public PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest request) {
        Users users = userService.getCurrentUser();
        request.setUserId(users.getId());
        return searchReservations(request);
    }

    private PageResponse<ReservationDTO> buildPageResponse(Page<Reservation> reservationPage) {
        List<ReservationDTO> reservationDTOS = reservationPage.getContent()
                .stream()
                .map(reservationMapper::toDto)
                .toList();

        PageResponse<ReservationDTO> pageResponse = new PageResponse<>();
        pageResponse.setContent(reservationDTOS);
        pageResponse.setPageNumber(reservationPage.getNumber());
        pageResponse.setPageSize(reservationPage.getSize());
        pageResponse.setTotalElements(reservationPage.getTotalElements());
        pageResponse.setTotalPages(reservationPage.getTotalPages());
        pageResponse.setLast(reservationPage.isLast());
        return pageResponse;
    }

    private Pageable createPageable(ReservationSearchRequest request) {
        Sort sort = "ASC".equalsIgnoreCase(request.getSortDirection())
                ? Sort.by(request.getSortBy()).ascending() : Sort.by(request.getSortBy()).descending();

        return PageRequest.of(request.getPage(), request.getPageSize(), sort);
    }
}
