package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.payload.dto.SubscriptionPlanDTO;

import java.util.List;

public interface SubscriptionPlanService {
    SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO planDTO);

    SubscriptionPlanDTO updateSubscriptionPlan(Long id, SubscriptionPlanDTO planDTO);

    void deleteSubscriptionPlan(Long id);

    List<SubscriptionPlanDTO> getAllSubscriptionPlans();

}
