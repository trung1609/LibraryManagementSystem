package com.example.LibraryManagementSystem.payload.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WishListDTO {
    private Long id;
    private Long userId;
    private String userName;
    private BookDTO book;
    private LocalDateTime addedAt;
    private String notes;
}
