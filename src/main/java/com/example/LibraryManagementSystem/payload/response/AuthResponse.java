package com.example.LibraryManagementSystem.payload.response;

import com.example.LibraryManagementSystem.payload.dto.UsersDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String jwt;
    private String message;
    private String title;
    private UsersDTO usersDTO;
}
