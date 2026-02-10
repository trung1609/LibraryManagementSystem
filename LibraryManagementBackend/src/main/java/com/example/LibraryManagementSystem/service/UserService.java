package com.example.LibraryManagementSystem.service;

import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.UsersDTO;

import java.util.List;

public interface UserService {
    public Users getCurrentUser();

    List<UsersDTO> getAllUsers();

    Users findById(Long id);
}
