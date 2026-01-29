package com.example.LibraryManagementSystem.controller;

import com.example.LibraryManagementSystem.exception.UserException;
import com.example.LibraryManagementSystem.payload.dto.UsersDTO;
import com.example.LibraryManagementSystem.payload.response.AuthResponse;
import com.example.LibraryManagementSystem.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
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
    public ResponseEntity<AuthResponse> signUpHandler(@RequestBody @Valid UsersDTO usersDTO) throws UserException {
        AuthResponse response = authService.signUp(usersDTO);
        return ResponseEntity.ok(response);
    }

}
