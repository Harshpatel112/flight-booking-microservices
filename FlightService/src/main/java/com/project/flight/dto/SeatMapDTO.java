package com.project.flight.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatMapDTO {
    
    // Flight Information
    private String flightNumber;
    private String aircraftType;
    private String aircraftModel;
    
    // Seat Map Configuration
    private int totalRows;
    private int seatsPerRow;
    private List<String> seatColumns; // A, B, C, D, E, F
    private Map<String, String> seatConfiguration; // 3-3, 2-4-2, etc.
    
    // Seat Classes
    private List<SeatClassDTO> seatClasses;
    
    // Seat Map Data
    private List<SeatRowDTO> seatRows;
    
    // Aircraft Layout
    private AircraftLayoutDTO aircraftLayout;
    
    // Seat Pricing
    private Map<String, BigDecimal> seatPricing;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatClassDTO {
        private String className; // ECONOMY, PREMIUM_ECONOMY, BUSINESS, FIRST
        private String classCode;
        private int startRow;
        private int endRow;
        private String seatPitch; // 32 inches
        private String seatWidth; // 18 inches
        private List<String> amenities;
        private String color; // For UI display
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatRowDTO {
        private int rowNumber;
        private String rowType; // NORMAL, EXIT_ROW, BULKHEAD, LAST_ROW
        private String seatClass;
        private List<SeatDTO> seats;
        private boolean isExitRow;
        private boolean hasBulkhead;
        private boolean hasExtraLegroom;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatDTO {
        private String seatNumber; // 12A, 15F
        private String seatLetter; // A, B, C, D, E, F
        private int rowNumber;
        private String seatType; // WINDOW, AISLE, MIDDLE
        private String status; // AVAILABLE, OCCUPIED, BLOCKED, SELECTED
        private String seatClass;
        
        // Seat Features
        private boolean isWindow;
        private boolean isAisle;
        private boolean isMiddle;
        private boolean isExitRow;
        private boolean hasExtraLegroom;
        private boolean isNearLavatory;
        private boolean isNearGalley;
        private boolean isBulkhead;
        private boolean isPreferredSeat;
        
        // Pricing
        private BigDecimal seatFee;
        private String currency;
        private boolean isFree;
        
        // Passenger Information (if occupied)
        private String passengerName;
        private String passengerType; // ADULT, CHILD, INFANT
        
        // Special Requirements
        private boolean requiresSpecialAssistance;
        private List<String> restrictions; // NO_INFANT, ADULT_ONLY, etc.
        
        // Visual Properties
        private String color; // For UI display
        private String icon; // For special seats
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AircraftLayoutDTO {
        private String aircraftType;
        private String manufacturer; // Boeing, Airbus
        private String model; // 737-800, A320
        private int totalSeats;
        private int economySeats;
        private int premiumEconomySeats;
        private int businessSeats;
        private int firstClassSeats;
        private int exitRows;
        private int lavatories;
        private int galleys;
        private boolean hasWifi;
        private boolean hasEntertainment;
        private boolean hasPowerOutlets;
        private List<String> amenities;
    }
}