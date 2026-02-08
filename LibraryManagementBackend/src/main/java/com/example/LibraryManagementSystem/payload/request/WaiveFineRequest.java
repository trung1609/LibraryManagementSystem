package com.example.LibraryManagementSystem.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WaiveFineRequest {
    @NotNull(message = "Fine Id cannot be null")
    private Long fineId;

    @NotBlank(message = "Reason cannot be blank")
    private String reason;
}
