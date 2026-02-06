package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Boolean existsAllByPlanCode(String planCode);
}
