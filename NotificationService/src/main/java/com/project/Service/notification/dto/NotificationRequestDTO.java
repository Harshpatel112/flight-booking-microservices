package com.project.Service.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {
    
    // Notification Type
    @NotBlank(message = "Notification type is required")
    private String notificationType; // EMAIL, SMS, PUSH, WHATSAPP
    
    // Recipient Information
    @NotEmpty(message = "At least one recipient is required")
    private List<RecipientDTO> recipients;
    
    // Message Content
    @NotBlank(message = "Subject is required")
    private String subject;
    
    private String message; // Plain text
    private String htmlMessage; // HTML content
    
    // Template Information
    private String templateName;
    private String templateLanguage = "EN";
    private Map<String, Object> templateVariables;
    
    // Notification Configuration
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT
    private String category; // BOOKING, PAYMENT, FLIGHT_UPDATE, PROMOTIONAL
    
    // Scheduling
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduleTime;
    
    private String timezone = "Asia/Kolkata";
    
    // Tracking and Analytics
    private String trackingId;
    private String campaignId;
    private String source; // BOOKING_SERVICE, PAYMENT_SERVICE, etc.
    private Map<String, String> metadata;
    
    // Delivery Options
    private boolean requireDeliveryReceipt;
    private boolean requireReadReceipt;
    private int maxRetryAttempts = 3;
    private String callbackUrl;
    
    // Attachments (for email)
    private List<AttachmentDTO> attachments;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecipientDTO {
        @NotBlank(message = "Recipient identifier is required")
        private String identifier; // Email, Phone, User ID
        
        private String name;
        private String email;
        private String phoneNumber;
        private String countryCode = "+91";
        private String language = "EN";
        private Map<String, Object> personalizedData;
    }
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttachmentDTO {
        @NotBlank(message = "File name is required")
        private String fileName;
        
        @NotBlank(message = "Content type is required")
        private String contentType;
        
        private byte[] content;
        private String contentUrl;
        private boolean isInline;
        private String contentId;
    }
}