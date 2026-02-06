package com.example.LibraryManagementSystem.mapper;

import com.example.LibraryManagementSystem.exception.SubscriptionException;
import com.example.LibraryManagementSystem.model.Subscription;
import com.example.LibraryManagementSystem.model.SubscriptionPlan;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.SubscriptionDTO;
import com.example.LibraryManagementSystem.repository.SubscriptionPlanRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import lombok.*;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    private final UserRepository userRepository;
    private final SubscriptionPlanRepository planRepository;

    public SubscriptionDTO toDto(Subscription subscription) {
        if (subscription == null) {
            return null;
        }

        SubscriptionDTO dto = new SubscriptionDTO();
        dto.setId(subscription.getId());

        if (subscription.getUsers() != null){
            dto.setUserId(subscription.getUsers().getId());
            dto.setUserName(subscription.getUsers().getFullName());
            dto.setUserEmail(subscription.getUsers().getEmail());
        }

        if (subscription.getPlan() != null){
            dto.setPlanId(subscription.getPlan().getId());
        }
        dto.setPlanName(subscription.getPlanName());
        dto.setPlanCode(subscription.getPlanCode());
        dto.setPrice(subscription.getPrice());
        dto.setStartDate(subscription.getStartDate());
        dto.setEndDate(subscription.getEndDate());
        dto.setIsActive(subscription.getIsActive());
        dto.setMaxBooksAllowed(subscription.getMaxBooksAllowed());
        dto.setMaxDaysPerBook(subscription.getMaxDaysPerBook());
        dto.setAutoRenew(subscription.getAutoRenew());
        dto.setCancelledAt(subscription.getCancelledAt());
        dto.setCancellationReason(subscription.getCancellationReason());
        dto.setNotes(subscription.getNotes());
        dto.setCreatedAt(subscription.getCreatedAt());
        dto.setUpdatedAt(subscription.getUpdatedAt());


        dto.setDaysRemaining(subscription.getDaysRemaining());
        dto.setIsValid(subscription.isValid());
        dto.setIsExpired(subscription.isExpired());

        return dto;

    }

    public Subscription toEntity(SubscriptionDTO dto) throws SubscriptionException {
        if (dto == null) {
            return null;
        }

        Subscription subscription = new Subscription();
        subscription.setId(dto.getId());

        if (dto.getUserId() != null) {
            Users users = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new SubscriptionException("User not found with id: " + dto.getUserId()));
            subscription.setUsers(users);
        }

        if (dto.getPlanId() != null) {
            SubscriptionPlan plan = planRepository.findById(dto.getPlanId())
                    .orElseThrow(() -> new SubscriptionException("Subscription Plan not found with id: " + dto.getPlanId())));
            subscription.setPlan(plan);
        }

        subscription.setPlanName(dto.getPlanName());
        subscription.setPlanCode(dto.getPlanCode());
        subscription.setPrice(dto.getPrice());
        subscription.setStartDate(dto.getStartDate());
        subscription.setEndDate(dto.getEndDate());
        subscription.setIsActive(dto.getIsActive() != null ? dto.getIsActive() : true);
        subscription.setMaxBooksAllowed(dto.getMaxBooksAllowed());
        subscription.setMaxDaysPerBook(dto.getMaxDaysPerBook());
        subscription.setAutoRenew(dto.getAutoRenew() != null ? dto.getAutoRenew() : false);
        subscription.setCancelledAt(dto.getCancelledAt());
        subscription.setCancellationReason(dto.getCancellationReason());
        subscription.setNotes(dto.getNotes());

        return subscription;
    }

    public List<SubscriptionDTO> toDTOList(List<Subscription> subscriptions){
        if (subscriptions == null) {
            return null;
        }
        return subscriptions.stream().map(this::toDto).toList();
    }

}
