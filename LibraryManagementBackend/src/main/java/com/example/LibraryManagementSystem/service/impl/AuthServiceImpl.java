package com.example.LibraryManagementSystem.service.impl;

import com.example.LibraryManagementSystem.config.JwtProvider;
import com.example.LibraryManagementSystem.domain.UserRole;
import com.example.LibraryManagementSystem.exception.UserException;
import com.example.LibraryManagementSystem.mapper.UserMapper;
import com.example.LibraryManagementSystem.model.PasswordResetToken;
import com.example.LibraryManagementSystem.model.Users;
import com.example.LibraryManagementSystem.payload.dto.UsersDTO;
import com.example.LibraryManagementSystem.payload.response.AuthResponse;
import com.example.LibraryManagementSystem.repository.PasswordResetTokenRepository;
import com.example.LibraryManagementSystem.repository.UserRepository;
import com.example.LibraryManagementSystem.service.AuthService;
import com.example.LibraryManagementSystem.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final CustomUserServiceImpl customerUser;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;

    @Override
    public AuthResponse login(String username, String password) throws UserException {
        Authentication authentication = authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        String role = authorities.iterator().next().getAuthority();
        String token = jwtProvider.generateToken(authentication);
        Users users = usersRepository.findByEmail(username);

        users.setLastLogin(LocalDateTime.now());
        usersRepository.save(users);

        AuthResponse authResponse = new AuthResponse();
        authResponse.setTitle("Login Successful");
        authResponse.setMessage("Welcome back " + users.getFullName());
        authResponse.setJwt(token);
        authResponse.setUsersDTO(UserMapper.toDTO(users));
        return null;
    }

    private Authentication authenticate(String username, String password) throws UserException {
        UserDetails userDetails = customerUser.loadUserByUsername(username);
        if (userDetails == null) {
            throw new UserException("User not found with email: " + username);
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new UserException("Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(username, null, userDetails.getAuthorities());
    }

    @Override
    public AuthResponse signUp(UsersDTO request) throws UserException {
        Users users = usersRepository.findByEmail(request.getEmail());

        if (users == null) {
            throw new UserException("Email is already registered");
        }

        Users createdUser = new Users();
        createdUser.setEmail(request.getEmail());
        createdUser.setPassword(passwordEncoder.encode(request.getPassword()));
        createdUser.setPhone(request.getPhone());
        createdUser.setFullName(request.getFullName());
        createdUser.setLastLogin(LocalDateTime.now());
        createdUser.setRole(UserRole.ROLE_USER);

        Users savedUser = usersRepository.save(createdUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmail(), savedUser.getPassword());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtProvider.generateToken(authentication);
        AuthResponse authResponse = new AuthResponse();
        authResponse.setJwt(jwt);
        authResponse.setTitle("Welcome " + createdUser.getFullName());
        authResponse.setMessage("Registration successful");
        authResponse.setUsersDTO(UserMapper.toDTO(savedUser));
        return authResponse;
    }

    @Transactional
    public void createPasswordResetToken(String email) throws UserException {
        String frontendUrl = "http://localhost:5173";
        Users users = usersRepository.findByEmail(email);
        if (users == null) {
            throw new UserException("User not found with email: " + email);
        }
        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .user(users)
                .expiryDate(LocalDateTime.now().plusMinutes(5))
                .build();

        passwordResetTokenRepository.save(passwordResetToken);
        String resetLink = frontendUrl + token;
        String subject = "Password Reset Request";
        String body = "You requested to reset your password. Use this link (valid 5 minutes): " + resetLink;

        emailService.sendEmail(users.getEmail(), subject, body);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws UserException {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token).orElseThrow(() -> new UserException("Token not valid"));
        if (resetToken.isExpired()) {
            passwordResetTokenRepository.delete(resetToken);
            throw new UserException("Token has expired");
        }

        Users users = resetToken.getUser();
        users.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(users);
        passwordResetTokenRepository.delete(resetToken);
    }
}
