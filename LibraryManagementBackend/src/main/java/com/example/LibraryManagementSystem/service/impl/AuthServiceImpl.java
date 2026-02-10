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
        return authResponse;
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

        if (users != null) {
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
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        String subject = "Password Reset Request - Library Management System";
        String body = buildPasswordResetEmail(users.getFullName(), resetLink);


        emailService.sendEmail(users.getEmail(), subject, body);
    }

    private String buildPasswordResetEmail(String fullName, String resetLink) {
        return """
                <html>
                <body style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                    <div style="max-width: 600px; margin: 0 auto; padding: 20px;">
                        <h2 style="color: #2c5282;">Password Reset Request</h2>
                        <p>Hello <strong>%s</strong>,</p>
                        <p>We received a request to reset your password for your Library Management System account.</p>
                        <p>Click the button below to reset your password:</p>
                        <div style="text-align: center; margin: 30px 0;">
                            <a href="%s" style="background-color: #3182ce; color: white; padding: 12px 24px; text-decoration: none; border-radius: 5px; font-weight: bold;">
                                Reset Password
                            </a>
                        </div>
                        <p>Or copy and paste this link into your browser:</p>
                        <p style="word-break: break-all; color: #3182ce;">%s</p>
                        <p><strong>This link will expire in 5 minutes.</strong></p>
                        <p>If you didn't request a password reset, please ignore this email or contact support if you have concerns.</p>
                        <hr style="border: none; border-top: 1px solid #eee; margin: 30px 0;">
                        <p style="color: #666; font-size: 12px;">This is an automated message from Library Management System. Please do not reply to this email.</p>
                    </div>
                </body>
                </html>
                """.formatted(fullName, resetLink, resetLink);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) throws UserException {
        if (token == null || token.startsWith("eyJ")) {
            throw new UserException("Invalid token format. Please use the reset link from your email.");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new UserException("Token not valid or has already been used"));
        if (resetToken.isExpired()) {
            passwordResetTokenRepository.delete(resetToken);
            throw new UserException("Token has expired. Please request a new password reset.");
        }

        Users users = resetToken.getUser();
        users.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(users);
        passwordResetTokenRepository.delete(resetToken);
    }
}
