package com.project.Service.notification.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingConfirmedEvent {
    
    private String eventId;
    private String bookingId;
    private Long userId;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPhoneNumber;
    private String flightNumber;
    private String airline;
    private String origin;
    private String destination;
    private String seatClass;
    private List<String> passengerNames;
    private BigDecimal totalAmount;
    private String currency;
    private String transactionId;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime bookingDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime departureTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
    
    private Map<String, Object> eventData;
}