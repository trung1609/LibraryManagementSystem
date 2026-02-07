package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.payload.dto.SubscriptionDTO;
import com.example.LibraryManagementSystem.payload.response.ApiResponse;
import com.example.LibraryManagementSystem.payload.response.PaymentInitiateResponse;
import com.example.LibraryManagementSystem.service.SubscriptionService;
import com.razorpay.RazorpayException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/subscribe")
    public ResponseEntity<PaymentInitiateResponse> subscribe(@Valid @RequestBody SubscriptionDTO subscriptionDTO) throws SubscriptionException, RazorpayException {
        return ResponseEntity.ok(subscriptionService.subscribe(subscriptionDTO));
    }

    @GetMapping("/admin")
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions() {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(subscriptionService.getAllSubscriptions(pageable));
    }

    @GetMapping("/admin/deactivate-expired")
    public ResponseEntity<?> deactivateExpiredSubscriptions() {
        subscriptionService.deactivateExpiredSubscriptions();
        ApiResponse response = new ApiResponse("Expired subscriptions deactivated successfully", true);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/active")
    public ResponseEntity<List<SubscriptionDTO>> getUsersActiveSubscriptions(
            @RequestParam(required = false) Long userId)
            throws SubscriptionException {
        return ResponseEntity.ok(subscriptionService.getUsersActiveSubscriptions(userId));
    }

    @PostMapping("/cancel/{subscriptionId}")
    public ResponseEntity<SubscriptionDTO> cancelSubscription(
            @PathVariable Long subscriptionId,
            @RequestParam(required = false) String reason
    ) throws SubscriptionException {
        return ResponseEntity.ok(subscriptionService.cancelSubscription(subscriptionId, reason));
    }

    @PostMapping("/activate")
    public ResponseEntity<SubscriptionDTO> activateSubscription(
            @RequestParam Long subscriptionId,
            @RequestParam Long paymentId
    ) throws SubscriptionException {
        return ResponseEntity.ok(subscriptionService.activateSubscription(subscriptionId, paymentId));
    }
}
