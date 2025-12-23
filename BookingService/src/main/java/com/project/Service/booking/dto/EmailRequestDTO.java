package com.project.Service.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Map;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequestDTO {
    
    @NotBlank(message = "Recipient email is required")
    @Email(message = "Valid email address is required")
    private String to;
    
    private List<String> cc;
    private List<String> bcc;
    
    @NotBlank(message = "Subject is required")
    private String subject;
    
    private String message; // Plain text message
    private String htmlMessage; // HTML formatted message
    
    // Template Information
    private String templateName;
    private Map<String, Object> templateVariables;
    
    // Attachments
    private List<EmailAttachmentDTO> attachments;
    
    // Email Configuration
    private String fromEmail;
    private String fromName;
    private String replyTo;
    
    // Priority and Delivery
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, URGENT
    private boolean requestDeliveryReceipt;
    private boolean requestReadReceipt;
    
    // Tracking
    private String trackingId;
    private String campaignId;
    private Map<String, String> metadata;
    
    // Scheduling
    private String scheduleTime; // ISO format for scheduled emails
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailAttachmentDTO {
        private String fileName;
        private String contentType;
        private byte[] content;
        private String contentId; // For inline attachments
        private boolean isInline;
    }
}
