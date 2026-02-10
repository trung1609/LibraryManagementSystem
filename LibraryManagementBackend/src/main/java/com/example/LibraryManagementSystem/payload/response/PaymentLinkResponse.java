package com.example.LibraryManagementSystem.payload.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentLinkResponse {
    private String paymentLinkUrl;
    private String paymentLinkId;
}
