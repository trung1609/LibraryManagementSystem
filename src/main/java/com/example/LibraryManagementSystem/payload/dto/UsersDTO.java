package com.example.LibraryManagementSystem.payload.dto;

import com.example.LibraryManagementSystem.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private Long id;

    private String email;

    private String password;

    private String fullName;

    private String phone;

    private UserRole role;

    private LocalDateTime lastLogin;
}
