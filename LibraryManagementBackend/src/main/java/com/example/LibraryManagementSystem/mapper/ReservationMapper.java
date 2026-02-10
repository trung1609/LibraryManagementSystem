package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.model.Reservation;
import com.example.LibraryManagementSystem.payload.dto.ReservationDTO;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;

@Component
public class ReservationMapper {

    public ReservationDTO toDto(Reservation reservation){
        if (reservation == null) {
            return null;
        }
        ReservationDTO dto = new ReservationDTO();
        dto.setId(reservation.getId());

        if (reservation.getUsers() != null){
            dto.setUserId(reservation.getUsers().getId());
            dto.setUserName(reservation.getUsers().getFullName());
            dto.setUserEmail(reservation.getUsers().getEmail());
        }
        if (reservation.getBook() != null){
            dto.setBookId(reservation.getBook().getId());
            dto.setBookTitle(reservation.getBook().getTitle());
            dto.setBookIsbn(reservation.getBook().getIsbn());
            dto.setBookAuthor(reservation.getBook().getAuthor());
            dto.setIsBookAvailable(reservation.getBook().getAvailableCopies()>0);
        }

        dto.setStatus(reservation.getStatus());
        dto.setReservedAt(reservation.getReservedAt());
        dto.setAvailableAt(reservation.getAvailableAt());
        dto.setAvailableUntil(reservation.getAvailableUntil());
        dto.setFulfilledAt(reservation.getFulfilledAt());
        dto.setCancelledAt(reservation.getCancelledAt());
        dto.setQueuePosition(reservation.getQueuePosition());
        dto.setNotificationSent(reservation.getNotificationSent());
        dto.setNotes(reservation.getNotes());
        dto.setCreatedAt(reservation.getCreatedAt());
        dto.setUpdatedAt(reservation.getUpdatedAt());

        dto.setExpired(reservation.hasExpired());
        dto.setCanBeCancelled(reservation.canBeCancelled());

        if (reservation.getAvailableUntil() != null){
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(reservation.getAvailableUntil())){
                long hours = Duration.between(now, reservation.getAvailableUntil()).toHours();
                dto.setHoursUntilExpired(hours);
            }else {
                dto.setHoursUntilExpired(0L);
            }
        }

        return dto;
    }
}
