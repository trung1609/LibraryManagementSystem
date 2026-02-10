package com.example.LibraryManagementSystem.payload.request;

import com.example.LibraryManagementSystem.domain.ReservationStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationSearchRequest {
    private Long userId;
    private Long bookId;

    private ReservationStatus status;

    private Boolean activeOnly;

    private Integer page = 0;
    private Integer pageSize = 20;
    private String sortBy = "reservedAt";
    private String sortDirection = "desc";
}
