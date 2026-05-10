package com.project.User.Service;

import com.project.User.Repository.UserRepository;
import com.project.User.dto.LoginRequestDTO;
import com.project.User.dto.LoginResponseDTO;
import com.project.User.dto.UserRegistrationDTO;
import com.project.User.dto.UserResponseDTO;
import com.project.User.enm.Role;
import com.project.User.exception.*;
import com.project.User.model.User;
import com.project.User.security.JwtUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        log.info("Registering new user with email: {}", registrationDTO.getEmail());

        // Validate password confirmation
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new IllegalArgumentException("Password and confirm password do not match");
        }

        // Check if user already exists
        if (userRepository.findByUsername(registrationDTO.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists: " + registrationDTO.getUsername());
        }

        if (userRepository.findByEmail(registrationDTO.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists: " + registrationDTO.getEmail());
        }

        // Create new user
        User user = User.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .firstName(registrationDTO.getFirstName())
                .middleName(registrationDTO.getMiddleName())
                .lastName(registrationDTO.getLastName())
                .phoneNumber(registrationDTO.getPhoneNumber())
                .alternatePhoneNumber(registrationDTO.getAlternatePhoneNumber())
                .title(registrationDTO.getTitle())
                .gender(registrationDTO.getGender())
                .dateOfBirth(registrationDTO.getDateOfBirth())
                .nationality(registrationDTO.getNationality())
                .maritalStatus(registrationDTO.getMaritalStatus())
                .role(Role.USER)
                .accountStatus("ACTIVE")
                .emailVerified(false)
                .phoneVerified(false)
                .registrationDate(LocalDateTime.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Generate JWT token for immediate login after registration
        String accessToken = jwtUtil.generateToken(savedUser.getUsername(), savedUser.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(savedUser.getUsername());

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400) // 24 hours in seconds
                .expiresAt(LocalDateTime.now().plusSeconds(86400))
                .user(convertToUserSummaryDTO(savedUser))
                .isFirstLogin(true)
                .requiresPasswordChange(false)
                .isTwoFactorRequired(false)
                .lastLoginTime(LocalDateTime.now())
                .accountStatus(savedUser.getAccountStatus())
                .isEmailVerified(savedUser.isEmailVerified())
                .isPhoneVerified(savedUser.isPhoneVerified())
                .roles(List.of(savedUser.getRole().name()))
                .permissions(getUserPermissions(savedUser.getRole()))
                .sessionId("SESSION_" + System.currentTimeMillis())
                .maxSessionDuration(1440) // 24 hours in minutes
                .isSecureSession(true)
                .welcomeMessage("Welcome to Flight Booking System, " + savedUser.getFirstName() + "!")
                .build();
    }

    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequest) {
        log.info("Authenticating user: {}", loginRequest.getUsernameOrEmail());

        User user = findUserByUsernameOrEmail(loginRequest.getUsernameOrEmail());

        // Check account status
        if ("LOCKED".equals(user.getAccountStatus())) {
            throw new AccountLockedException("Account is locked. Please contact support.");
        }

        if ("INACTIVE".equals(user.getAccountStatus())) {
            throw new InvalidCredentialsException("Account is inactive. Please contact support.");
        }

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            log.warn("Invalid password attempt for user: {}", loginRequest.getUsernameOrEmail());
            throw new InvalidCredentialsException("Invalid username/email or password");
        }

        // Update last login
        user.setLastLoginDate(LocalDateTime.now());
        userRepository.save(user);

        // Generate JWT token
        String accessToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        log.info("User authenticated successfully: {}", user.getUsername());

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(86400) // 24 hours in seconds
                .expiresAt(LocalDateTime.now().plusSeconds(86400))
                .user(convertToUserSummaryDTO(user))
                .isFirstLogin(user.getLastLoginDate() == null)
                .requiresPasswordChange(false)
                .isTwoFactorRequired(false)
                .lastLoginTime(user.getLastLoginDate())
                .accountStatus(user.getAccountStatus())
                .isEmailVerified(user.isEmailVerified())
                .isPhoneVerified(user.isPhoneVerified())
                .roles(List.of(user.getRole().name()))
                .permissions(getUserPermissions(user.getRole()))
                .sessionId("SESSION_" + System.currentTimeMillis())
                .maxSessionDuration(1440) // 24 hours in minutes
                .isSecureSession(true)
                .welcomeMessage("Welcome back, " + user.getFirstName() + "!")
                .build();
    }

    public UserResponseDTO getUserProfileByToken(String token) {
        String username = jwtUtil.extractUsername(token);
        User user = findUserByUsername(username);
        return convertToUserResponseDTO(user);
    }

    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
        return convertToUserResponseDTO(user);
    }

    public UserResponseDTO updateUserProfile(String token, UserRegistrationDTO updateDTO) {
        String username = jwtUtil.extractUsername(token);
        User user = findUserByUsername(username);

        // Update user fields
        user.setFirstName(updateDTO.getFirstName());
        user.setMiddleName(updateDTO.getMiddleName());
        user.setLastName(updateDTO.getLastName());
        user.setPhoneNumber(updateDTO.getPhoneNumber());
        user.setAlternatePhoneNumber(updateDTO.getAlternatePhoneNumber());
        user.setTitle(updateDTO.getTitle());
        user.setGender(updateDTO.getGender());
        user.setDateOfBirth(updateDTO.getDateOfBirth());
        user.setNationality(updateDTO.getNationality());
        user.setMaritalStatus(updateDTO.getMaritalStatus());

        User updatedUser = userRepository.save(user);
        log.info("User profile updated for: {}", username);

        return convertToUserResponseDTO(updatedUser);
    }

    public LoginResponseDTO refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new TokenExpiredException("Refresh token is invalid or expired");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        User user = findUserByUsername(username);

        String newAccessToken = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        return LoginResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(86400)
                .expiresAt(LocalDateTime.now().plusSeconds(86400))
                .user(convertToUserSummaryDTO(user))
                .build();
    }

    public void logoutUser(String token) {
        String username = jwtUtil.extractUsername(token);
        log.info("User logged out: {}", username);
        // In a real implementation, you would add the token to a blacklist
    }

    // Helper methods
    private User findUserByUsernameOrEmail(String identifier) {
        return userRepository.findByUsername(identifier)
                .orElseGet(() -> userRepository.findByEmail(identifier)
                        .orElseThrow(() -> new InvalidCredentialsException("Invalid username/email or password")));
    }

    private User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .middleName(user.getMiddleName())
                .lastName(user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .alternatePhoneNumber(user.getAlternatePhoneNumber())
                .title(user.getTitle())
                .gender(user.getGender())
                .dateOfBirth(user.getDateOfBirth())
                .nationality(user.getNationality())
                .maritalStatus(user.getMaritalStatus())
                .accountStatus(user.getAccountStatus())
                .isEmailVerified(user.isEmailVerified())
                .isPhoneVerified(user.isPhoneVerified())
                .registrationDate(user.getRegistrationDate())
                .lastLoginDate(user.getLastLoginDate())
                .build();
    }

    private LoginResponseDTO.UserSummaryDTO convertToUserSummaryDTO(User user) {
        return LoginResponseDTO.UserSummaryDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.getFirstName() + " " + user.getLastName())
                .phoneNumber(user.getPhoneNumber())
                .membershipTier("SILVER")
                .preferredLanguage("EN")
                .timezone("Asia/Kolkata")
                .build();
    }

    private List<String> getUserPermissions(Role role) {
        return switch (role) {
            case ADMIN -> List.of("READ", "WRITE", "DELETE", "ADMIN");
            case USER -> List.of("READ", "WRITE");
        };
    }
}