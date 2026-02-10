package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.payload.dto.SubscriptionPlanDTO;
import com.example.LibraryManagementSystem.payload.response.ApiResponse;
import com.example.LibraryManagementSystem.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription-plans")
public class SubscriptionPlanController {

    private final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    public ResponseEntity<List<SubscriptionPlanDTO>> getAllSubscriptionPlans() {
        return ResponseEntity.ok(subscriptionPlanService.getAllSubscriptionPlans());
    }

    @PostMapping("/admin/create")
    public ResponseEntity<SubscriptionPlanDTO> createSubscriptionPlan(@Valid @RequestBody SubscriptionPlanDTO planDTO) {
        return ResponseEntity.ok(subscriptionPlanService.createSubscriptionPlan(planDTO));
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<SubscriptionPlanDTO> updateSubscriptionPlan(@PathVariable Long id, @Valid @RequestBody SubscriptionPlanDTO planDTO) {
        return ResponseEntity.ok(subscriptionPlanService.updateSubscriptionPlan(id, planDTO));
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteSubscriptionPlan(@PathVariable Long id) {
        subscriptionPlanService.deleteSubscriptionPlan(id);
        ApiResponse response = new ApiResponse("Subscription Plan deleted successfully", true);
        return ResponseEntity.ok(response);
    }

}
