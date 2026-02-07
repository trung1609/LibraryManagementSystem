package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.domain.PaymentGateway;
import com.example.LibraryManagementSystem.domain.PaymentType;
import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.mapper.SubscriptionMapper;
import com.example.LibraryManagementSystem.model.Subscription;
import com.example.LibraryManagementSystem.model.SubscriptionPlan;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.SubscriptionDTO;
import com.example.LibraryManagementSystem.payload.request.PaymentInitiateRequest;
import com.example.LibraryManagementSystem.payload.response.PaymentInitiateResponse;
import com.example.LibraryManagementSystem.repository.SubscriptionPlanRepository;
import com.example.LibraryManagementSystem.repository.SubscriptionRepository;
import com.example.LibraryManagementSystem.service.PaymentService;
import com.example.LibraryManagementSystem.service.SubscriptionService;
import com.example.LibraryManagementSystem.service.UserService;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final PaymentService paymentService;

    @Override
    public PaymentInitiateResponse subscribe(SubscriptionDTO subscriptionDTO) throws SubscriptionException, RazorpayException {
        Users users = userService.getCurrentUser();

        SubscriptionPlan plan = subscriptionPlanRepository.findById(subscriptionDTO.getPlanId()).orElseThrow(() -> new RuntimeException("Plan not found with id: " + subscriptionDTO.getPlanId()));

        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO, plan, users);
        subscription.initializeFromPlan();
        subscription.setIsActive(false);
        Subscription savedSubscription =  subscriptionRepository.save(subscription);

        PaymentInitiateRequest paymentInitiateRequest = PaymentInitiateRequest.builder()
                .userId(users.getId())
                .subscriptionId(subscription.getId())
                .paymentType(PaymentType.MEMBERSHIP)
                .gateway(PaymentGateway.RAZORPAY)
                .amount(subscription.getPrice())
                .description("Library Subscription - " + plan.getName())
                .build();

        return paymentService.initiatePayment(paymentInitiateRequest);
    }

    @Override
    public SubscriptionDTO getUsersActiveSubscriptions(Long userId) throws SubscriptionException {
        Users users = userService.getCurrentUser();
        Subscription subscriptions = subscriptionRepository
                .findActiveSubscriptionsByUserId(users.getId(), LocalDate.now());
        if (subscriptions == null){
            throw new SubscriptionException("No active subscriptions found");
        }
        return subscriptionMapper.toDto(subscriptions);
    }

    @Override
    public SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new SubscriptionException("Subscription not found with id: " + subscriptionId));

        if (!subscription.getIsActive()){
            throw new RuntimeException("Subscription is already inactive");
        }

        subscription.setIsActive(false);
        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancellationReason(reason != null ? reason : "No reason provided");
        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException {

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new SubscriptionException("Subscription not found with id: " + subscriptionId));

        subscription.setIsActive(true);
        return subscriptionMapper.toDto(subscriptionRepository.save(subscription));
    }

    @Override
    public List<SubscriptionDTO> getAllSubscriptions(Pageable pageable) {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptionMapper.toDTOList(subscriptions);
    }

    @Override
    public void deactivateExpiredSubscriptions() {
        List<Subscription> invalidSubscriptions = subscriptionRepository
                .findInvalidActiveSubscriptions(LocalDate.now());

        System.out.println("Today: " + LocalDate.now());
        System.out.println("Found " + invalidSubscriptions.size() + " invalid subscriptions");
        invalidSubscriptions.forEach(subscription -> {
            subscription.setIsActive(false);
        });
        subscriptionRepository.saveAll(invalidSubscriptions);
    }
}
