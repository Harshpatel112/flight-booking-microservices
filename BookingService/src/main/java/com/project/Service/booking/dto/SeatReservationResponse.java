package com.project.Service.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatReservationResponse {
    
    // Reservation Information
    private String reservationId;
    private String flightNumber;
    private boolean success;
    private String message;
    private String status; // CONFIRMED, PENDING, FAILED, EXPIRED
    
    // Seat Information
    private List<ReservedSeatDTO> reservedSeats;
    private int totalSeatsReserved;
    private String seatClass;
    
    // Pricing Information
    private BigDecimal totalSeatFees;
    private BigDecimal baseFare;
    private BigDecimal totalAmount;
    private String currency = "INR";
    
    // Timing Information
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime reservationTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiryTime;
    
    private int holdDurationMinutes = 15; // How long seats are held
    
    // Flight Information
    private String airline;
    private String origin;
    private String destination;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureDateTime;
    
    // Passenger Information
    private List<String> passengerNames;
    
    // Additional Information
    private String bookingReference;
    private List<String> warnings;
    private List<String> importantNotes;
    
    // Error Information (if failed)
    private String errorCode;
    private String errorMessage;
    private List<String> unavailableSeats;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservedSeatDTO {
        private String seatNumber;
        private String passengerName;
        private String seatType; // WINDOW, AISLE, MIDDLE
        private BigDecimal seatFee;
        private boolean isExtraLegroom;
        private boolean isEmergencyExit;
        private boolean isPreferred;
        private String seatFeatures;
        private String status; // RESERVED, CONFIRMED, BLOCKED
    }
}