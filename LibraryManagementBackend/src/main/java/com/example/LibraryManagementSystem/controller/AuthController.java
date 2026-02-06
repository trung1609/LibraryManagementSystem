package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exception.UserException;
import com.example.LibraryManagementSystem.payload.dto.UsersDTO;
import com.example.LibraryManagementSystem.payload.request.ForgotPasswordRequest;
import com.example.LibraryManagementSystem.payload.request.LoginRequest;
import com.example.LibraryManagementSystem.payload.request.ResetPasswordRequest;
import com.example.LibraryManagementSystem.payload.response.ApiResponse;
import com.example.LibraryManagementSystem.payload.response.AuthResponse;
import com.example.LibraryManagementSystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signUpHandler(@Valid @RequestBody UsersDTO usersDTO) throws UserException {
        AuthResponse response = authService.signUp(usersDTO);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginHandler(@Valid @RequestBody LoginRequest loginRequest) throws UserException {
        AuthResponse response = authService.login(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) throws UserException {
        authService.createPasswordResetToken(request.getEmail());
        ApiResponse response = new ApiResponse("A Reset link was sent to your email.", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest request) throws UserException {
        authService.resetPassword(request.getToken(), request.getPassword());
        ApiResponse response = new ApiResponse("Password reset successfully.", true);
        return ResponseEntity.ok(response);
    }

}
