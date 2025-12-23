package com.project.Service.booking.dto;

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
public class FlightDTO {
    
    // Basic Flight Information
    private String flightNumber;
    private String airline;
    private String airlineCode;
    private String aircraftType;
    
    // Route Information
    private String origin;
    private String destination;
    private String originAirportCode;
    private String destinationAirportCode;
    private String originCity;
    private String destinationCity;
    
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
    
    private String duration;
    private int durationMinutes;
    private int stops;
    private List<String> stopoverAirports;
    
    // Pricing Information
    private Map<String, BigDecimal> pricePerClass;
    private BigDecimal basePrice;
    private BigDecimal totalPrice;
    private String currency = "INR";
    
    // Seat Information
    private Map<String, Integer> availableSeatsPerClass;
    private Map<String, Integer> totalSeatsPerClass;
    private int totalAvailableSeats;
    
    // Flight Status
    private String status; // SCHEDULED, DELAYED, CANCELLED, BOARDING, DEPARTED
    private String flightType; // DOMESTIC, INTERNATIONAL
    
    // Additional Information
    private List<String> amenities;
    private String baggage;
    private boolean isRefundable;
    private boolean isReschedulable;
    private String cancellationPolicy;
    
    // Booking Information
    private int seatsLeft;
    private boolean isAvailableForBooking;
    private String unavailabilityReason;
    
    // Promotional Information
    private boolean hasOffer;
    private String offerText;
    private BigDecimal discountAmount;
}
