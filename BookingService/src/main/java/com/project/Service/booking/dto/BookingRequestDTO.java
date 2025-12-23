package com.project.Service.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestDTO {
    
    // Flight Information
    @NotBlank(message = "Flight number is required")
    private String flightNumber;
    
    @NotBlank(message = "Travel class is required")
    private String travelClass; // ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
    
    // Passenger Information
    @NotEmpty(message = "At least one passenger is required")
    @Size(max = 9, message = "Maximum 9 passengers allowed")
    private List<PassengerDTO> passengers;
    
    // Contact Information
    @NotBlank(message = "Contact email is required")
    @Email(message = "Valid email is required")
    private String contactEmail;
    
    @NotBlank(message = "Contact phone is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Valid phone number is required")
    private String contactPhone;
    
    // Seat Selection
    private List<SeatSelectionDTO> seatSelections;
    
    // Add-ons
    private List<String> mealPreferences;
    private List<String> specialRequests; // Wheelchair, Extra legroom, etc.
    private boolean needsSpecialAssistance;
    private String specialAssistanceDetails;
    
    // Baggage
    private List<BaggageSelectionDTO> baggageSelections;
    
    // Insurance
    private boolean travelInsurance;
    private String insuranceType;
    
    // Pricing
    private BigDecimal totalAmount;
    private String currency = "INR";
    
    // Payment
    private String paymentMethod; // CARD, UPI, NETBANKING, WALLET
    
    // Booking Preferences
    private boolean sendSMS = true;
    private boolean sendEmail = true;
    private String preferredLanguage = "EN";
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerDTO {
        
        @NotBlank(message = "Passenger type is required")
        private String passengerType; // ADULT, CHILD, INFANT
        
        @NotBlank(message = "Title is required")
        private String title; // Mr, Ms, Mrs, Dr, etc.
        
        @NotBlank(message = "First name is required")
        @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
        private String firstName;
        
        @Size(max = 50, message = "Middle name cannot exceed 50 characters")
        private String middleName;
        
        @NotBlank(message = "Last name is required")
        @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
        private String lastName;
        
        @NotNull(message = "Date of birth is required")
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate dateOfBirth;
        
        @NotBlank(message = "Gender is required")
        private String gender; // MALE, FEMALE, OTHER
        
        @NotBlank(message = "Nationality is required")
        private String nationality;
        
        // For International Flights
        private String passportNumber;
        private String passportCountry;
        
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate passportExpiryDate;
        
        // Frequent Flyer
        private String frequentFlyerNumber;
        private String frequentFlyerAirline;
        
        // Special Requirements
        private String mealPreference;
        private List<String> specialRequests;
        
        // Seat Preference
        private String seatPreference; // WINDOW, AISLE, MIDDLE
        
        // Contact (for lead passenger)
        private String email;
        private String phone;
        
        // Address (if required)
        private String address;
        private String city;
        private String state;
        private String country;
        private String pincode;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatSelectionDTO {
        private String passengerName;
        private String seatNumber; // 12A, 15F, etc.
        private String seatType; // WINDOW, AISLE, MIDDLE
        private BigDecimal seatFee;
        private boolean isEmergencyExit;
        private boolean hasExtraLegroom;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaggageSelectionDTO {
        private String passengerName;
        private String baggageType; // CABIN, CHECKIN, EXCESS
        private int weight; // in KG
        private BigDecimal fee;
        private String description;
    }
}