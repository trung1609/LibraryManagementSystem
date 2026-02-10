package com.example.LibraryManagementSystem.payload.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentVerifyRequest {
    private String razorpayPaymentId;

    private String stripePaymentIntentId;
    private String stripePaymentIntentStatus;
}
