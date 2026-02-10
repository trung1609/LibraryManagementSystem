package com.example.LibraryManagementSystem.repository;

import com.example.LibraryManagementSystem.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository <Subscription, Long> {

    @Query("select s from Subscription s where s.users.id = :user_id " +
            "and s.isActive = true " +
            "and s.startDate <= :today " +
            "and s.endDate >= :today " +
            "order by s.endDate desc")
    Subscription findActiveSubscriptionsByUserId(@Param("user_id") Long userId,
                                                       @Param("today")LocalDate today);

    @Query("select s from Subscription s where s.isActive = true " +
            "and (s.endDate < :today or s.startDate > :today)")
    List<Subscription> findInvalidActiveSubscriptions(
            @Param("today")LocalDate today
    );
}
