package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.mapper.SubscriptionPlanMapper;
import com.example.LibraryManagementSystem.model.SubscriptionPlan;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.SubscriptionPlanDTO;
import com.example.LibraryManagementSystem.repository.SubscriptionPlanRepository;
import com.example.LibraryManagementSystem.service.SubscriptionPlanService;
import com.example.LibraryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionPlanServiceImpl implements SubscriptionPlanService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionPlanMapper subscriptionPlanMapper;
    private final UserService userService;

    @Override
    public SubscriptionPlanDTO createSubscriptionPlan(SubscriptionPlanDTO planDTO) {
        if (subscriptionPlanRepository.existsAllByPlanCode(planDTO.getPlanCode())){
            throw new RuntimeException("Subscription Plan with plan code " + planDTO.getPlanCode() + " already exists.");
        }
        SubscriptionPlan plan = subscriptionPlanMapper.toEntity(planDTO);
        Users currentUser = userService.getCurrentUser();
        plan.setCreatedBy(currentUser.getFullName());
        plan.setUpdatedBy(currentUser.getFullName());


        SubscriptionPlan subscriptionPlan = subscriptionPlanRepository.save(plan);
        return subscriptionPlanMapper.toDto(subscriptionPlan);
    }

    @Override
    public SubscriptionPlanDTO updateSubscriptionPlan(Long id, SubscriptionPlanDTO planDTO) {
        SubscriptionPlan existingPlan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription Plan not found with id: " + id));
        subscriptionPlanMapper.updateEntity(planDTO, existingPlan);
        Users currentUser = userService.getCurrentUser();
        existingPlan.setUpdatedBy(currentUser.getFullName());
        return subscriptionPlanMapper.toDto(subscriptionPlanRepository.save(existingPlan));
    }

    @Override
    public void deleteSubscriptionPlan(Long id) {
        SubscriptionPlan existingPlan = subscriptionPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subscription Plan not found with id: " + id));
        subscriptionPlanRepository.delete(existingPlan);
    }

    @Override
    public List<SubscriptionPlanDTO> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAll().stream()
                .map(subscriptionPlanMapper::toDto)
                .toList();
    }
}
