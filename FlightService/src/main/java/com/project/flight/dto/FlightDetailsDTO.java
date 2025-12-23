package com.project.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDetailsDTO {
    
    // Basic Flight Information
    private String flightNumber;
    private String airline;
    private String airlineCode; // AI, 6E, SG, UK, etc.
    private String aircraftType; // Boeing 737, Airbus A320, etc.
    
    // Route Information
    private String origin;
    private String destination;
    private String originAirportCode; // DEL, BOM, BLR, etc.
    private String destinationAirportCode;
    private String originAirportName; // Indira Gandhi International Airport
    private String destinationAirportName;
    private String originCity;
    private String destinationCity;
    private String originCountry;
    private String destinationCountry;
    
    // Timing Information
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate departureDate;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime departureTime;
    
    @JsonFormat(pattern = "HH:mm")
    private LocalTime arrivalTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureDateTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime arrivalDateTime;
    
    private String duration; // 2h 30m
    private int durationMinutes; // 150
    private boolean isOvernight;
    private int stops; // 0 for direct, 1+ for connecting
    private List<String> stopoverAirports;
    
    // Pricing Information
    private Map<String, FlightClassDetailsDTO> classDetails;
    private BigDecimal basePrice;
    private BigDecimal totalPrice; // Including taxes and fees
    private String currency = "INR";
    
    // Seat Information
    private Map<String, Integer> availableSeatsPerClass;
    private Map<String, Integer> totalSeatsPerClass;
    private int totalAvailableSeats;
    
    // Additional Information
    private String status; // SCHEDULED, DELAYED, CANCELLED, BOARDING, DEPARTED
    private String flightType; // DOMESTIC, INTERNATIONAL
    private boolean isCodeshare;
    private String operatingAirline;
    private List<String> amenities; // WiFi, Meals, Entertainment, etc.
    private String baggage; // 15kg check-in, 7kg cabin
    private boolean isRefundable;
    private boolean isReschedulable;
    private String cancellationPolicy;
    
    // Booking Information
    private int seatsLeft; // Seats left at current price
    private String bookingClass; // Y, M, B, etc.
    private boolean isAvailableForBooking;
    private String unavailabilityReason;
    
    // Promotional Information
    private boolean hasOffer;
    private String offerText;
    private BigDecimal discountAmount;
    private String discountPercentage;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightClassDetailsDTO {
        private String className; // ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
        private String classCode; // Y, W, J, F
        private BigDecimal price;
        private BigDecimal basePrice;
        private BigDecimal taxes;
        private BigDecimal fees;
        private int availableSeats;
        private String seatPitch; // 32 inches
        private String seatWidth; // 18 inches
        private List<String> amenities;
        private String mealType; // Complimentary, Paid, Premium
        private String baggageAllowance;
        private boolean isRefundable;
        private String cancellationFee;
        private String rescheduleFee;
        private boolean hasLounge;
        private boolean hasPriorityBoarding;
        private boolean hasPriorityBaggage;
    }
}
