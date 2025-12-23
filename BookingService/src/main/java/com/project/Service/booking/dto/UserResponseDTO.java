package com.project.Service.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {
    
    // Basic Information
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String fullName;
    private String phoneNumber;
    private String alternatePhoneNumber;
    
    // Personal Information
    private String title; // Mr, Ms, Mrs, Dr
    private String gender; // MALE, FEMALE, OTHER
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    
    private String nationality;
    private String maritalStatus;
    
    // Address Information
    private AddressDTO address;
    
    // Travel Preferences
    private TravelPreferencesDTO travelPreferences;
    
    // Frequent Flyer Information
    private List<FrequentFlyerDTO> frequentFlyerPrograms;
    
    // Account Information
    private String accountStatus; // ACTIVE, INACTIVE, SUSPENDED
    private String membershipTier; // SILVER, GOLD, PLATINUM
    private boolean isEmailVerified;
    private boolean isPhoneVerified;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registrationDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastLoginDate;
    
    // Passport Information (for international travel)
    private List<PassportDTO> passports;
    
    // Emergency Contact
    private EmergencyContactDTO emergencyContact;
    
    // Preferences
    private NotificationPreferencesDTO notificationPreferences;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AddressDTO {
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String country;
        private String pincode;
        private String addressType; // HOME, OFFICE, OTHER
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TravelPreferencesDTO {
        private String preferredClass; // ECONOMY, BUSINESS, FIRST
        private String seatPreference; // WINDOW, AISLE, MIDDLE
        private String mealPreference; // VEG, NON_VEG, VEGAN, JAIN
        private List<String> specialRequests;
        private String preferredAirline;
        private boolean needsSpecialAssistance;
        private String specialAssistanceDetails;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FrequentFlyerDTO {
        private String airline;
        private String airlineCode;
        private String membershipNumber;
        private String membershipTier;
        private int milesBalance;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate expiryDate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassportDTO {
        private String passportNumber;
        private String issuingCountry;
        private String nationality;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate issueDate;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate expiryDate;
        
        private String placeOfBirth;
        private boolean isPrimary;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmergencyContactDTO {
        private String name;
        private String relationship;
        private String phoneNumber;
        private String email;
        private String address;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationPreferencesDTO {
        private boolean emailNotifications;
        private boolean smsNotifications;
        private boolean pushNotifications;
        private boolean promotionalEmails;
        private boolean bookingUpdates;
        private boolean flightAlerts;
        private String preferredLanguage;
    }
}
