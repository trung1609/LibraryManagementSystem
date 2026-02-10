package com.example.LibraryManagementSystem.payload.dto;

import com.example.LibraryManagementSystem.domain.PaymentGateway;
import com.example.LibraryManagementSystem.domain.PaymentStatus;
import com.example.LibraryManagementSystem.domain.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    @NotNull(message = "User Id cannot be null")
    private Long userId;

    private String userName;
    private String userEmail;

    private Long bookLoanId;

    private Long subscriptionId;

    @NotNull(message = "Payment Type cannot be null")
    private PaymentType paymentType;

    private PaymentStatus paymentStatus;

    @NotNull(message = "Payment Gateway cannot be null")
    private PaymentGateway gateway;

    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Long amount;

    private String transactionId;

    private String gatewayPaymentId;

    private String gatewayOrderId;

    private String gatewaySignature;


    private String description;

    private String failureReason;

    private Integer retryCount;

    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
