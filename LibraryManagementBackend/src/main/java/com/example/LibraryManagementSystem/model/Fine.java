package com.example.LibraryManagementSystem.model;

import com.example.LibraryManagementSystem.domain.FineStatus;
import com.example.LibraryManagementSystem.domain.FineType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Users users;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BookLoan bookLoan;

    @Enumerated(EnumType.STRING)
    private FineType type;

    @Column(nullable = false)
    private Long amount;


    @Enumerated(EnumType.STRING)
    private FineStatus status;

    @Column(length = 500)
    private String reason;

    @Column(length = 1000)
    private String notes;

    @ManyToOne
    private Users waivedBy;

    @Column(name = "waived_at")
    private LocalDateTime waivedAt;

    @Column(name = "waiver_reason", length = 500)
    private String waiverReason;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @ManyToOne
    @JoinColumn(name = "processed_by_user_id")
    private Users processedBy;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void applyPayment(Long amount){
        if (amount == null || amount<=0){
            throw new IllegalArgumentException("Amount cannot be null or negative");
        }

        this.status = FineStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    public void waive(Users admin, String reason){
        this.status = FineStatus.WAIVED;
        this.waivedBy = admin;
        this.waivedAt = LocalDateTime.now();
        this.waiverReason = reason;
    }
}
