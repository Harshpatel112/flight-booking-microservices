package com.project.Service.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDTO {
    
    // Payment Information
    private String paymentId;
    private String transactionId;
    private String orderId;
    private String bookingId;
    private Long userId;
    
    // Payment Status
    private String status; // INITIATED, PROCESSING, SUCCESS, FAILED, CANCELLED, REFUNDED
    private String paymentStatus; // For backward compatibility
    private String gatewayStatus;
    private String gatewayTransactionId;
    
    // Amount Information
    private BigDecimal amount;
    private BigDecimal paidAmount;
    private BigDecimal refundedAmount;
    private String currency;
    
    // Payment Method Information
    private String paymentMethod;
    private PaymentMethodDetailsDTO paymentMethodDetails;
    
    // Timing Information
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime initiatedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime completedAt;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expiresAt;
    
    // Gateway Information
    private String paymentGateway; // RAZORPAY, PAYU, CCAVENUE, STRIPE
    private String gatewayOrderId;
    private String gatewayPaymentId;
    
    // Customer Information
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    
    // Transaction Details
    private List<TransactionDetailsDTO> transactionHistory;
    
    // Error Information
    private String errorCode;
    private String errorMessage;
    private String failureReason;
    
    // Receipt Information
    private String receiptUrl;
    private String invoiceUrl;
    
    // Refund Information
    private List<RefundDetailsDTO> refunds;
    
    // Additional Information
    private Map<String, String> metadata;
    private String description;
    
    // Security Information
    private String riskScore;
    private boolean isFraudulent;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PaymentMethodDetailsDTO {
        private String type; // CARD, UPI, NETBANKING, WALLET, EMI
        
        // Card Details (masked)
        private String cardNumber; // ****1234
        private String cardType; // VISA, MASTERCARD
        private String cardNetwork;
        private String issuingBank;
        private String cardCountry;
        
        // UPI Details
        private String upiId; // Masked
        private String upiApp;
        
        // Net Banking Details
        private String bankCode;
        private String bankName;
        
        // Wallet Details
        private String walletProvider;
        
        // EMI Details
        private Integer emiTenure;
        private BigDecimal emiAmount;
        private BigDecimal interestRate;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionDetailsDTO {
        private String transactionId;
        private String status;
        private BigDecimal amount;
        private String currency;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime timestamp;
        
        private String description;
        private String gatewayResponse;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefundDetailsDTO {
        private String refundId;
        private String refundTransactionId;
        private BigDecimal refundAmount;
        private String refundStatus;
        private String refundReason;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime refundInitiatedAt;
        
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime refundCompletedAt;
        
        private String refundMethod;
        private String refundReference;
    }
}