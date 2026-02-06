package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.payload.dto.SubscriptionDTO;

import java.awt.print.Pageable;
import java.util.List;

public interface SubscriptionService {

    SubscriptionDTO subscribe(SubscriptionDTO subscriptionDTO);

    SubscriptionDTO getUsersActiveSubscription(Long userId);

    SubscriptionDTO cancelSubscription(Long subscriptionId, String reason);

    SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId);

    List<SubscriptionDTO> getAllSubscriptions(Pageable pageable);
}
