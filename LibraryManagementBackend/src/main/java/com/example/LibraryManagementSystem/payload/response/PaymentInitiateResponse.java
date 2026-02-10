package com.example.LibraryManagementSystem.payload.response;

import com.example.LibraryManagementSystem.domain.PaymentGateway;
import com.example.LibraryManagementSystem.model.Users;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInitiateResponse {
    private Long paymentId;
    private PaymentGateway gateway;
    private String transactionId;

    private String razorpayOrderId;
    private Long amount;
    private String description;
    private String checkoutUrl;
    private String message;
    private Boolean success;
}
