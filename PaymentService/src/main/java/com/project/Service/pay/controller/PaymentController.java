package com.project.Service.pay.controller;

import com.project.Service.payment.dto.PaymentRequestDTO;
import com.project.Service.payment.dto.PaymentResponseDTO;
import com.project.Service.pay.service.PaymentService;
import com.project.Service.pay.service.RazorpayService;
import com.project.Service.pay.client.PaymentVerificationRequest;
import com.razorpay.RazorpayException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment Management", description = "APIs for payment processing and management")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private RazorpayService razorpayService;
    
    @Autowired(required = false)
    private PaymentService paymentService;

    // Legacy Razorpay endpoints (keeping for backward compatibility)
    @PostMapping("/create-order")
    @Operation(summary = "Create Razorpay order", description = "Create Razorpay order for payment")
    public ResponseEntity<String> createOrder(@RequestParam int amount, @RequestParam Long bookingId) throws RazorpayException {
        return ResponseEntity.ok(razorpayService.createOrder(amount, bookingId));
    }

    @PostMapping("/verify")
    @Operation(summary = "Verify Razorpay payment", description = "Verify Razorpay payment signature")
    public ResponseEntity<String> verifyAndConfirmPayment(@RequestBody PaymentVerificationRequest request) throws RazorpayException {
        return ResponseEntity.ok(razorpayService.verifyAndConfirmPayment(request));
    }

    // New comprehensive payment endpoints
    @PostMapping("/initiate")
    @Operation(summary = "Initiate payment", description = "Initiate payment for booking")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Payment initiated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid payment data"),
        @ApiResponse(responseCode = "404", description = "Booking not found")
    })
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@Valid @RequestBody PaymentRequestDTO paymentRequest) {
        if (paymentService != null) {
            PaymentResponseDTO response = paymentService.initiatePayment(paymentRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/process")
    @Operation(summary = "Process payment", description = "Process payment with payment gateway")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment processed successfully"),
        @ApiResponse(responseCode = "400", description = "Payment processing failed"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentResponseDTO> processPayment(@Valid @RequestBody PaymentRequestDTO paymentRequest) {
        if (paymentService != null) {
            PaymentResponseDTO response = paymentService.processPayment(paymentRequest);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/{paymentId}")
    @Operation(summary = "Get payment details", description = "Get payment details by payment ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Payment details retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Payment not found")
    })
    public ResponseEntity<PaymentResponseDTO> getPaymentDetails(@PathVariable String paymentId) {
        if (paymentService != null) {
            PaymentResponseDTO response = paymentService.getPaymentDetails(paymentId);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @GetMapping("/booking/{bookingId}")
    @Operation(summary = "Get payments by booking", description = "Get all payments for a specific booking")
    public ResponseEntity<List<PaymentResponseDTO>> getPaymentsByBooking(@PathVariable String bookingId) {
        if (paymentService != null) {
            List<PaymentResponseDTO> payments = paymentService.getPaymentsByBookingId(bookingId);
            return ResponseEntity.ok(payments);
        }
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }

    @PostMapping("/webhook/razorpay")
    @Operation(summary = "Razorpay webhook", description = "Handle Razorpay webhook notifications")
    public ResponseEntity<String> handleRazorpayWebhook(
            @RequestBody Map<String, Object> payload,
            @RequestHeader(value = "X-Razorpay-Signature", required = false) String signature) {
        
        if (paymentService != null) {
            paymentService.handleRazorpayWebhook(payload, signature);
            return ResponseEntity.ok("Webhook processed successfully");
        }
        return ResponseEntity.ok("Webhook received");
    }

    @GetMapping("/methods")
    @Operation(summary = "Get payment methods", description = "Get available payment methods")
    public ResponseEntity<List<String>> getPaymentMethods() {
        if (paymentService != null) {
            List<String> methods = paymentService.getAvailablePaymentMethods();
            return ResponseEntity.ok(methods);
        }
        // Return default payment methods
        return ResponseEntity.ok(List.of("CARD", "UPI", "NETBANKING", "WALLET"));
    }

    @GetMapping("/status/{paymentId}")
    @Operation(summary = "Get payment status", description = "Get current payment status")
    public ResponseEntity<String> getPaymentStatus(@PathVariable String paymentId) {
        // Implementation for getting payment status
        return ResponseEntity.ok("PENDING"); // Default response
    }
}
