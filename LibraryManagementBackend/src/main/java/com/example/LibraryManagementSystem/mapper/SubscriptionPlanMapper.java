package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.model.SubscriptionPlan;
import com.example.LibraryManagementSystem.payload.dto.SubscriptionPlanDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubscriptionPlanMapper {

    public SubscriptionPlanDTO toDto(SubscriptionPlan plan) {
        if (plan == null) {
            return null;
        }

        return SubscriptionPlanDTO.builder()
                .id(plan.getId())
                .planCode(plan.getPlanCode())
                .name(plan.getName())
                .description(plan.getDescription())
                .durationDays(plan.getDurationDays())
                .price(plan.getPrice())
                .currency(plan.getCurrency())
                .maxBooksAllowed(plan.getMaxBooksAllowed())
                .maxDaysPerBook(plan.getMaxDaysPerBook())
                .displayOrder(plan.getDisplayOrder())
                .isActive(plan.getIsActive())
                .isFeatured(plan.getIsFeatured())
                .badgeText(plan.getBadgeText())
                .adminNotes(plan.getAdminNotes())
                .createdAt(plan.getCreatedAt())
                .updatedAt(plan.getUpdatedAt())
                .createdBy(plan.getCreatedBy())
                .updatedBy(plan.getUpdatedBy())
                .build();
    }

    public SubscriptionPlan toEntity(SubscriptionPlanDTO dto){
        if (dto == null) {
            return null;
        }

        return SubscriptionPlan.builder()
                .id(dto.getId())
                .planCode(dto.getPlanCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .durationDays(dto.getDurationDays())
                .price(dto.getPrice())
                .currency(dto.getCurrency() != null ? dto.getCurrency() : "INR")
                .maxBooksAllowed(dto.getMaxBooksAllowed())
                .maxDaysPerBook(dto.getMaxDaysPerBook())
                .displayOrder(dto.getDisplayOrder() != null ? dto.getDisplayOrder() : 0)
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .isFeatured(dto.getIsFeatured() != null ? dto.getIsFeatured() : false)
                .badgeText(dto.getBadgeText())
                .adminNotes(dto.getAdminNotes())
                .createdAt(dto.getCreatedAt())
                .updatedAt(dto.getUpdatedAt())
                .createdBy(dto.getCreatedBy())
                .updatedBy(dto.getUpdatedBy())
                .build();
    }

    public void updateEntity(SubscriptionPlanDTO dto, SubscriptionPlan entity){
        if (dto == null || entity == null) {
            return;
        }

        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getDurationDays() != null) {
            entity.setDurationDays(dto.getDurationDays());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getMaxBooksAllowed() != null) {
            entity.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        }
        if (dto.getMaxDaysPerBook() != null) {
            entity.setMaxDaysPerBook(dto.getMaxDaysPerBook());
        }
        if (dto.getDisplayOrder() != null) {
            entity.setDisplayOrder(dto.getDisplayOrder());
        }
        if (dto.getIsActive() != null) {
            entity.setIsActive(dto.getIsActive());
        }
        if (dto.getIsFeatured() != null) {
            entity.setIsFeatured(dto.getIsFeatured());
        }
        if (dto.getBadgeText() != null) {
            entity.setBadgeText(dto.getBadgeText());
        }
        if (dto.getAdminNotes() != null) {
            entity.setAdminNotes(dto.getAdminNotes());
        }
        if (dto.getUpdatedAt() != null){
            entity.setUpdatedAt(dto.getUpdatedAt());
        }

    }
}
