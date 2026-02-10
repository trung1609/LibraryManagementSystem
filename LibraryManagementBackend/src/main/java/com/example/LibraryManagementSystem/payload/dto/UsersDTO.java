package com.example.LibraryManagementSystem.payload.dto;

import com.example.LibraryManagementSystem.domain.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersDTO {
    private Long id;

    @NotNull(message = "Email cannot be null")
    private String email;

    @NotNull(message = "Password cannot be null")
    private String password;

    @NotNull(message = "Full name cannot be null")
    private String fullName;

    private String phone;

    private UserRole role;

    private LocalDateTime lastLogin;
}
