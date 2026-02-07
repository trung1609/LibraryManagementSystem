package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.payload.dto.SubscriptionDTO;
import com.example.LibraryManagementSystem.payload.response.PaymentInitiateResponse;
import com.razorpay.RazorpayException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService {

    PaymentInitiateResponse subscribe(SubscriptionDTO subscriptionDTO) throws SubscriptionException, RazorpayException;

    List<SubscriptionDTO> getUsersActiveSubscriptions(Long userId) throws SubscriptionException;

    SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException;

    SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException;

    List<SubscriptionDTO> getAllSubscriptions(Pageable pageable);

    void deactivateExpiredSubscriptions();
}
