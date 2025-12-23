package com.project.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightSearchRequestDTO {
    
    @NotBlank(message = "Origin city is required")
    private String origin;
    
    @NotBlank(message = "Destination city is required")
    private String destination;
    
    @NotNull(message = "Departure date is required")
    private LocalDate departureDate;
    
    private LocalDate returnDate; // For round trip
    
    @Min(value = 1, message = "At least 1 adult passenger required")
    @Max(value = 9, message = "Maximum 9 passengers allowed")
    private int adults = 1;
    
    @Min(value = 0, message = "Children count cannot be negative")
    @Max(value = 8, message = "Maximum 8 children allowed")
    private int children = 0;
    
    @Min(value = 0, message = "Infants count cannot be negative")
    @Max(value = 2, message = "Maximum 2 infants allowed")
    private int infants = 0;
    
    private String travelClass = "ECONOMY"; // ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
    
    private String tripType = "ONE_WAY"; // ONE_WAY, ROUND_TRIP, MULTI_CITY
    
    private boolean directFlightsOnly = false;
    
    private String preferredAirline;
    
    private String sortBy = "PRICE"; // PRICE, DURATION, DEPARTURE_TIME, ARRIVAL_TIME
    
    private String sortOrder = "ASC"; // ASC, DESC
}