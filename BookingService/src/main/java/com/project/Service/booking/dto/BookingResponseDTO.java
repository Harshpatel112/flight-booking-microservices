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
public class BookingResponseDTO {
    
    // Booking Information
    private String bookingId;
    private String pnr; // Passenger Name Record
    private String bookingReference;
    private String status; // CONFIRMED, PENDING, CANCELLED, COMPLETED
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookingDateTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDateTime;
    
    // Flight Information
    private FlightBookingDetailsDTO flightDetails;
    
    // Passenger Information
    private List<PassengerBookingDetailsDTO> passengers;
    
    // Contact Information
    private ContactDetailsDTO contactDetails;
    
    // Pricing Information
    private PricingDetailsDTO pricingDetails;
    
    // Payment Information
    private PaymentDetailsDTO paymentDetails;
    
    // Booking Status
    private String bookingStatus;
    private String paymentStatus;
    private String ticketStatus;
    
    // Important Information
    private List<String> importantNotes;
    private List<String> cancellationPolicy;
    private List<String> changePolicy;
    
    // Check-in Information
    private boolean canCheckIn;
    private String checkInUrl;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime checkInOpenTime;
    
    // Baggage Information
    private List<BaggageDetailsDTO> baggageDetails;
    
    // Seat Information
    private List<SeatDetailsDTO> seatDetails;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlightBookingDetailsDTO {
        private String flightNumber;
        private String airline;
        private String airlineCode;
        private String aircraftType;
        private String origin;
        private String destination;
        private String originAirportCode;
        private String destinationAirportCode;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime departureDateTime;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        private LocalDateTime arrivalDateTime;
        
        private String duration;
        private String travelClass;
        private String terminal;
        private String gate;
        private String flightStatus;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PassengerBookingDetailsDTO {
        private String passengerType;
        private String title;
        private String firstName;
        private String middleName;
        private String lastName;
        private String fullName;
        private String ticketNumber;
        private String seatNumber;
        private String mealPreference;
        private String baggageAllowance;
        private String frequentFlyerNumber;
        private List<String> specialServices;
        private String eTicketUrl;
        private String boardingPassUrl;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactDetailsDTO {
        private String email;
        private String phone;
        private String alternatePhone;
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
    public static class PricingDetailsDTO {
        private BigDecimal basePrice;
        private BigDecimal taxes;
        private BigDecimal fees;
        private BigDecimal seatFees;
        private BigDecimal baggageFees;
        private BigDecimal mealFees;
        private BigDecimal insuranceFees;
        private BigDecimal discountAmount;
        private BigDecimal totalAmount;
        private String currency;
        private List<PriceBreakdownDTO> priceBreakdown;
        
        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PriceBreakdownDTO {
            private String description;
            private BigDecimal amount;
            private String type; // BASE, TAX, FEE, DISCOUNT
        }
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentDetailsDTO {
        private String paymentId;
        private String paymentMethod;
        private String paymentStatus;
        private BigDecimal paidAmount;
        private String currency;
        private String transactionId;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime paymentDateTime;
        
        private String paymentGateway;
        private String cardLastFourDigits;
        private String bankName;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BaggageDetailsDTO {
        private String passengerName;
        private String baggageType;
        private String allowance;
        private BigDecimal excessFee;
        private String description;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SeatDetailsDTO {
        private String passengerName;
        private String seatNumber;
        private String seatType;
        private BigDecimal seatFee;
        private boolean isEmergencyExit;
        private boolean hasExtraLegroom;
        private String seatFeatures;
    }
}