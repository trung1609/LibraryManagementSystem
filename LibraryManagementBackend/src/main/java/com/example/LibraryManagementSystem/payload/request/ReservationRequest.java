package com.example.LibraryManagementSystem.payload.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequest {
    @NotNull(message = "Book ID is mandatory")
    private Long bookId;

    private String notes;
}
