package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.domain.ReservationStatus;
import com.example.LibraryManagementSystem.exception.BookException;
import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.payload.dto.ReservationDTO;
import com.example.LibraryManagementSystem.payload.request.ReservationRequest;
import com.example.LibraryManagementSystem.payload.request.ReservationSearchRequest;
import com.example.LibraryManagementSystem.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationRequest request){
        ReservationDTO dto = reservationService.createReservation(request);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createReservationForUser(@PathVariable Long userId, @Valid @RequestBody ReservationRequest request){
        ReservationDTO dto = reservationService.createReservationForUsers(userId, request);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id){
        ReservationDTO dto = reservationService.cancelReservation(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/fulfill")
    public ResponseEntity<?> fulfillReservation(@PathVariable Long id) throws SubscriptionException, BookException {
        ReservationDTO dto = reservationService.fulfillReservation(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyReservations(
            @RequestParam(required = false) ReservationStatus status,

            @RequestParam(required = false) Boolean activeOnly,

            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "reservedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        ReservationSearchRequest searchRequest = new ReservationSearchRequest();
        searchRequest.setStatus(status);
        searchRequest.setActiveOnly(activeOnly);
        searchRequest.setPage(page);
        searchRequest.setPageSize(pageSize);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);
        return ResponseEntity.ok(reservationService.getMyReservations(searchRequest));
    }

    @GetMapping
    public ResponseEntity<?> getAllReservations(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) Long bookId,
            @RequestParam(required = false) ReservationStatus status,
            @RequestParam(required = false) Boolean activeOnly,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize,
            @RequestParam(defaultValue = "reservedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ){
        ReservationSearchRequest searchRequest = new ReservationSearchRequest();
        searchRequest.setUserId(userId);
        searchRequest.setBookId(bookId);
        searchRequest.setStatus(status);
        searchRequest.setActiveOnly(activeOnly);
        searchRequest.setPage(page);
        searchRequest.setPageSize(pageSize);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);
        return ResponseEntity.ok(reservationService.searchReservations(searchRequest));
    }
}
