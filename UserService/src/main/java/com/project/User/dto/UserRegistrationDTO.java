package com.project.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {
    
    // Basic Information
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username can only contain letters, numbers, dots, underscores, and hyphens")
    private String username;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Valid email address is required")
    private String email;
    
    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$", 
             message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;
    
    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;
    
    // Personal Information
    @NotBlank(message = "Title is required")
    private String title; // Mr, Ms, Mrs, Dr
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    
    @Size(max = 50, message = "Middle name cannot exceed 50 characters")
    private String middleName;
    
    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Valid phone number is required")
    private String phoneNumber;
    
    private String alternatePhoneNumber;
    
    @NotBlank(message = "Gender is required")
    private String gender; // MALE, FEMALE, OTHER
    
    @NotNull(message = "Date of birth is required")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    @NotBlank(message = "Nationality is required")
    private String nationality;
    
    private String maritalStatus; // SINGLE, MARRIED, DIVORCED, WIDOWED
    
    // Address Information
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String pincode;
    
    // Travel Preferences
    private String preferredClass = "ECONOMY";
    private String seatPreference = "WINDOW";
    private String mealPreference = "VEG";
    private String preferredLanguage = "EN";
    
    // Terms and Conditions
    @AssertTrue(message = "You must accept the terms and conditions")
    private boolean acceptTermsAndConditions;
    
    @AssertTrue(message = "You must accept the privacy policy")
    private boolean acceptPrivacyPolicy;
    
    // Marketing Preferences
    private boolean subscribeToNewsletter = true;
    private boolean allowPromotionalEmails = true;
    private boolean allowSMSNotifications = true;
    
    // Referral Information
    private String referralCode;
    private String referredBy;
    
    // Device Information
    private String deviceId;
    private String deviceType; // WEB, MOBILE_APP, TABLET
    private String ipAddress;
    private String userAgent;
}