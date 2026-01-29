package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.exception.UserException;
import com.example.LibraryManagementSystem.payload.dto.UsersDTO;
import com.example.LibraryManagementSystem.payload.response.AuthResponse;

public interface AuthService {

    AuthResponse login(String username, String password) throws UserException;

    AuthResponse signUp(UsersDTO request) throws UserException;

    void createPasswordResetToken(String email) throws UserException;

    void resetPassword(String token, String newPassword) throws UserException;


}
