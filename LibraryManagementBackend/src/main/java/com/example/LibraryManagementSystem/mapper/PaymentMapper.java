package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.model.Payment;
import com.example.LibraryManagementSystem.payload.dto.PaymentDTO;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentDTO toDTO(Payment payment){
        if (payment == null) {
            return null;
        }
        PaymentDTO dto = new PaymentDTO();
        dto.setId(payment.getId());

        if (payment.getUsers() != null){
            dto.setUserId(payment.getUsers().getId());
            dto.setUserName(payment.getUsers().getFullName());
            dto.setUserEmail(payment.getUsers().getEmail());
        }

//        if (payment.getBookLoan() != null){
//            dto.setBookLoanId(payment.getBookLoan().getId);
//        }

        if (payment.getSubscription() != null){
            dto.setSubscriptionId(payment.getSubscription().getId());
        }

        dto.setPaymentType(payment.getPaymentType());
        dto.setPaymentStatus(payment.getPaymentStatus());
        dto.setGateway(payment.getGateway());
        dto.setAmount(payment.getAmount());
        dto.setTransactionId(payment.getTransactionId());
        dto.setGatewayPaymentId(payment.getGatewayPaymentId());
        dto.setGatewayOrderId(payment.getGatewayOrderId());
        dto.setGatewaySignature(payment.getGatewaySignature());
        dto.setDescription(payment.getDescription());
        dto.setFailureReason(payment.getFailureReason());
        dto.setInitiatedAt(payment.getInitiatedAt());
        dto.setCompletedAt(payment.getCompletedAt());
        dto.setCreatedAt(payment.getCreatedAt());
        dto.setUpdatedAt(payment.getUpdatedAt());
        return dto;
    }
}
