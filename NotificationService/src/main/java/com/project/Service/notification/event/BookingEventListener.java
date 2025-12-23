package com.project.Service.notification.event;

import com.project.Service.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventListener {
    
    private final NotificationService notificationService;
    
    @KafkaListener(topics = "${app.events.topics.booking-confirmed}", groupId = "notification-service-group")
    public void handleBookingConfirmed(@Payload BookingConfirmedEvent event,
                                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     Acknowledgment acknowledgment) {
        try {
            log.info("Received booking confirmed event for booking: {}", event.getBookingId());
            
            // Send booking confirmation email
            notificationService.sendBookingConfirmationEmail(event);
            
            // Send SMS if enabled
            notificationService.sendBookingConfirmationSMS(event);
            
            acknowledgment.acknowledge();
            log.info("Successfully processed booking confirmed event for booking: {}", event.getBookingId());
            
        } catch (Exception e) {
            log.error("Error processing booking confirmed event for booking: {}, Error: {}", 
                    event.getBookingId(), e.getMessage());
            // Don't acknowledge - message will be retried
        }
    }
    
    @KafkaListener(topics = "${app.events.topics.booking-cancelled}", groupId = "notification-service-group")
    public void handleBookingCancelled(@Payload BookingCancelledEvent event,
                                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     Acknowledgment acknowledgment) {
        try {
            log.info("Received booking cancelled event for booking: {}", event.getBookingId());
            
            // Send booking cancellation email
            notificationService.sendBookingCancellationEmail(event);
            
            // Send SMS if enabled
            notificationService.sendBookingCancellationSMS(event);
            
            acknowledgment.acknowledge();
            log.info("Successfully processed booking cancelled event for booking: {}", event.getBookingId());
            
        } catch (Exception e) {
            log.error("Error processing booking cancelled event for booking: {}, Error: {}", 
                    event.getBookingId(), e.getMessage());
            // Don't acknowledge - message will be retried
        }
    }
}