package com.example.LibraryManagementSystem.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private Users users;

    @ManyToOne
    private Book book;

    @CreationTimestamp
    private LocalDateTime addedAt;

    @Column(length = 500)
    private String notes;
}
