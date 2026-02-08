package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.payload.dto.ReservationDTO;
import com.example.LibraryManagementSystem.payload.request.ReservationRequest;
import com.example.LibraryManagementSystem.payload.request.ReservationSearchRequest;
import com.example.LibraryManagementSystem.payload.response.PageResponse;
import com.example.LibraryManagementSystem.repository.ReservationRepository;
import com.example.LibraryManagementSystem.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public ReservationDTO createReservation(ReservationRequest request) {
        return null;
    }

    @Override
    public ReservationDTO createReservationForUsers(Long userId, ReservationRequest request) {
        return null;
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
