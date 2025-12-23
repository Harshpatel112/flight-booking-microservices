package com.project.Service.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDTO {
    
    // Booking Information
    @NotBlank(message = "Booking ID is required")
    private String bookingId;
    
    @NotNull(message = "User ID is required")
    private Long userId;
    
    // Amount Information
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "1.0", message = "Amount must be greater than 0")
    private BigDecimal amount;
    
    @NotBlank(message = "Currency is required")
    private String currency = "INR";
    
    // Payment Method
    @NotBlank(message = "Payment method is required")
    private String paymentMethod; // CARD, UPI, NETBANKING, WALLET, EMI
    
    // Card Information (for card payments)
    private CardDetailsDTO cardDetails;
    
    // UPI Information (for UPI payments)
    private UPIDetailsDTO upiDetails;
    
    // Net Banking Information
    private NetBankingDetailsDTO netBankingDetails;
    
    // Wallet Information
    private WalletDetailsDTO walletDetails;
    
    // EMI Information
    private EMIDetailsDTO emiDetails;
    
    // Customer Information
    @NotBlank(message = "Customer name is required")
    private String customerName;
    
    @NotBlank(message = "Customer email is required")
    @Email(message = "Valid email is required")
    private String customerEmail;
    
    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Valid phone number is required")
    private String customerPhone;
    
    // Billing Address
    private BillingAddressDTO billingAddress;
    
    // Additional Information
    private String description;
    private Map<String, String> metadata;
    private String callbackUrl;
    private String cancelUrl;
    
    // Security
    private String deviceId;
    private String ipAddress;
    private String userAgent;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CardDetailsDTO {
        @NotBlank(message = "Card number is required")
        @Pattern(regexp = "^[0-9]{13,19}$", message = "Valid card number is required")
        private String cardNumber;
        
        @NotBlank(message = "Cardholder name is required")
        private String cardholderName;
        
        @NotBlank(message = "Expiry month is required")
        @Pattern(regexp = "^(0[1-9]|1[0-2])$", message = "Valid expiry month is required")
        private String expiryMonth;
        
        @NotBlank(message = "Expiry year is required")
        @Pattern(regexp = "^[0-9]{4}$", message = "Valid expiry year is required")
        private String expiryYear;
        
        @NotBlank(message = "CVV is required")
        @Pattern(regexp = "^[0-9]{3,4}$", message = "Valid CVV is required")
        private String cvv;
        
        private String cardType; // VISA, MASTERCARD, AMEX, RUPAY
        private boolean saveCard;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UPIDetailsDTO {
        @NotBlank(message = "UPI ID is required")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+$", message = "Valid UPI ID is required")
        private String upiId;
        
        private String upiApp; // GPAY, PHONEPE, PAYTM, BHIM
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NetBankingDetailsDTO {
        @NotBlank(message = "Bank code is required")
        private String bankCode;
        
        private String bankName;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WalletDetailsDTO {
        @NotBlank(message = "Wallet provider is required")
        private String walletProvider; // PAYTM, MOBIKWIK, FREECHARGE, AMAZONPAY
        
        private String walletPhone;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EMIDetailsDTO {
        @NotBlank(message = "Bank code is required")
        private String bankCode;
        
        @NotNull(message = "EMI tenure is required")
        @Min(value = 3, message = "Minimum EMI tenure is 3 months")
        @Max(value = 24, message = "Maximum EMI tenure is 24 months")
        private Integer tenure;
        
        private BigDecimal interestRate;
        private BigDecimal emiAmount;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillingAddressDTO {
        private String addressLine1;
        private String addressLine2;
        private String city;
        private String state;
        private String country;
        private String pincode;
    }
}