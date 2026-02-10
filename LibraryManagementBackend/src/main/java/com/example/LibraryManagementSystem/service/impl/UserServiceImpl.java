package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.mapper.UserMapper;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.UsersDTO;
import com.example.LibraryManagementSystem.repository.UserRepository;
import com.example.LibraryManagementSystem.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Users getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Users users = userRepository.findByEmail(email);
        if (users == null) {
            throw new RuntimeException("User not found with email: " + email);
        }

        return users;
    }

    @Override
    public List<UsersDTO> getAllUsers() {
        List<Users> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    @Override
    public Users findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}
