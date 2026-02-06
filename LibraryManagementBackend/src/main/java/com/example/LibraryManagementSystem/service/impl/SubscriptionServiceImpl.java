package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.payload.dto.SubscriptionDTO;
import com.example.LibraryManagementSystem.repository.SubscriptionRepository;
import com.example.LibraryManagementSystem.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Override
    public SubscriptionDTO subscribe(SubscriptionDTO subscriptionDTO) {
        return null;
    }

    @Override
    public SubscriptionDTO getUsersActiveSubscription(Long userId) {
        return null;
    }

    @Override
    public SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) {
        return null;
    }

    @Override
    public SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) {
        return null;
    }

    @Override
    public List<SubscriptionDTO> getAllSubscriptions(Pageable pageable) {
        return List.of();
    }
}
