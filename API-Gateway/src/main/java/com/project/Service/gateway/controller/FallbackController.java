package com.project.Service.gateway.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fallback")
@Slf4j
public class FallbackController {

    @GetMapping("/user-service")
    @PostMapping("/user-service")
    public ResponseEntity<Map<String, Object>> userServiceFallback() {
        log.warn("User service is currently unavailable - fallback triggered");
        return createFallbackResponse("User Service", "User service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/flight-service")
    @PostMapping("/flight-service")
    public ResponseEntity<Map<String, Object>> flightServiceFallback() {
        log.warn("Flight service is currently unavailable - fallback triggered");
        return createFallbackResponse("Flight Service", "Flight service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/booking-service")
    @PostMapping("/booking-service")
    public ResponseEntity<Map<String, Object>> bookingServiceFallback() {
        log.warn("Booking service is currently unavailable - fallback triggered");
        return createFallbackResponse("Booking Service", "Booking service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/payment-service")
    @PostMapping("/payment-service")
    public ResponseEntity<Map<String, Object>> paymentServiceFallback() {
        log.warn("Payment service is currently unavailable - fallback triggered");
        return createFallbackResponse("Payment Service", "Payment service is temporarily unavailable. Please try again later.");
    }

    @GetMapping("/notification-service")
    @PostMapping("/notification-service")
    public ResponseEntity<Map<String, Object>> notificationServiceFallback() {
        log.warn("Notification service is currently unavailable - fallback triggered");
        return createFallbackResponse("Notification Service", "Notification service is temporarily unavailable. Please try again later.");
    }

    private ResponseEntity<Map<String, Object>> createFallbackResponse(String serviceName, String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("service", serviceName);
        response.put("message", message);
        response.put("timestamp", LocalDateTime.now());
        response.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());
        
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}