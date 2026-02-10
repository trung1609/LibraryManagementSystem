package com.example.LibraryManagementSystem.payload.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    @NotNull(message = "User ID is mandatory")
    private Long bookId;

    @Min(value = 1, message = "Minimum checkout days is 1")
    private Integer checkoutDays = 14; //default 14 days

    private String notes;

}
