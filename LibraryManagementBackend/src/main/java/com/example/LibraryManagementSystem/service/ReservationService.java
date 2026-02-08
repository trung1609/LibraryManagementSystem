package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.payload.dto.ReservationDTO;
import com.example.LibraryManagementSystem.payload.request.ReservationRequest;
import com.example.LibraryManagementSystem.payload.request.ReservationSearchRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;

public interface ReservationService {
    ReservationDTO createReservation(ReservationRequest request);

    ReservationDTO createReservationForUsers(Long userId, ReservationRequest request);

    ReservationDTO cancelReservation(Long reservationId);

    ReservationDTO fulfillReservation(Long reservationId);

    PageResponse<ReservationDTO> searchReservations(ReservationSearchRequest request);

    PageResponse<ReservationDTO> getMyReservations(ReservationSearchRequest request);
}
