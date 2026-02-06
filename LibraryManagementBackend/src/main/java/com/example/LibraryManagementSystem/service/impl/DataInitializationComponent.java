package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.domain.UserRole;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeAdminUser();
    }

    private void initializeAdminUser() {
        String adminEmail = "admin@gmail.com";
        String adminPassword = "123456";
        if (userRepository.findByEmail(adminEmail) == null) {
            Users users = Users.builder()
                    .password(passwordEncoder.encode(adminPassword))
                    .email(adminEmail)
                    .fullName("Admin")
                    .role(UserRole.ROLE_ADMIN)
                    .build();
            Users admin = userRepository.save(users);
        }
    }
}
