package com.project.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    
    // Authentication Information
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn; // in seconds
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;
    
    // User Information
    private UserSummaryDTO user;
    
    // Login Information
    private boolean isFirstLogin;
    private boolean requiresPasswordChange;
    private boolean isTwoFactorRequired;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginTime;
    
    private String lastLoginLocation;
    private String lastLoginDevice;
    
    // Account Status
    private String accountStatus; // ACTIVE, INACTIVE, SUSPENDED, LOCKED
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    
    // Permissions and Roles
    private List<String> roles;
    private List<String> permissions;
    
    // Session Information
    private String sessionId;
    private int maxSessionDuration; // in minutes
    
    // Security Information
    private boolean isSecureSession;
    private List<String> securityWarnings;
    
    // Additional Information
    private String welcomeMessage;
    private List<String> notifications;
    private int unreadNotificationCount;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserSummaryDTO {
        private Long id;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private String fullName;
        private String phoneNumber;
        private String profilePictureUrl;
        private String membershipTier;
        private String preferredLanguage;
        private String timezone;
    }
}