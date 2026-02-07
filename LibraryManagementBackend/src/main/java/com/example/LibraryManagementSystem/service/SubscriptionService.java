package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.payload.dto.SubscriptionDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SubscriptionService {

    SubscriptionDTO subscribe(SubscriptionDTO subscriptionDTO) throws SubscriptionException;

    List<SubscriptionDTO> getUsersActiveSubscriptions(Long userId) throws SubscriptionException;

    SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException;

    SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException;

    List<SubscriptionDTO> getAllSubscriptions(Pageable pageable);

    void deactivateExpiredSubscriptions();
}
