package com.project.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    
    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;
    
    @NotBlank(message = "Password is required")
    private String password;
    
    // Remember me functionality
    private boolean rememberMe = false;
    
    // Device information for security
    private String deviceId;
    private String deviceName;
    private String deviceType; // WEB, MOBILE_APP, TABLET
    private String ipAddress;
    private String userAgent;
    private String location;
    
    // Two-factor authentication
    private String twoFactorCode;
    private boolean isTwoFactorEnabled = false;
}