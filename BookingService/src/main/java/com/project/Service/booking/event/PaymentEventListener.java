package com.project.Service.booking.event;

import com.project.Service.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventListener {
    
    private final BookingService bookingService;
    private final EventPublisher eventPublisher;
    
    @KafkaListener(topics = "${app.events.topics.payment-completed}", groupId = "booking-service-group")
    public void handlePaymentCompleted(@Payload PaymentCompletedEvent event,
                                     @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                     Acknowledgment acknowledgment) {
        try {
            log.info("Received payment completed event for booking: {}", event.getBookingId());
            
            // Update booking status to CONFIRMED
            bookingService.confirmBooking(event.getBookingId(), event.getTransactionId());
            
            // Publish booking confirmed event for notification service
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("paymentId", event.getPaymentId());
            eventData.put("transactionId", event.getTransactionId());
            eventData.put("amount", event.getAmount());
            eventData.put("currency", event.getCurrency());
            
            eventPublisher.publishBookingConfirmed(
                event.getBookingId(), 
                event.getUserId(), 
                event.getFlightNumber(),
                eventData
            );
            
            acknowledgment.acknowledge();
            log.info("Successfully processed payment completed event for booking: {}", event.getBookingId());
            
        } catch (Exception e) {
            log.error("Error processing payment completed event for booking: {}, Error: {}", 
                    event.getBookingId(), e.getMessage());
            // Don't acknowledge - message will be retried
        }
    }
    
    @KafkaListener(topics = "${app.events.topics.payment-failed}", groupId = "booking-service-group")
    public void handlePaymentFailed(@Payload PaymentFailedEvent event,
                                  @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                                  Acknowledgment acknowledgment) {
        try {
            log.info("Received payment failed event for booking: {}", event.getBookingId());
            
            // Update booking status to PAYMENT_FAILED and release seats
            bookingService.handlePaymentFailure(event.getBookingId(), event.getReason());
            
            // Publish booking cancelled event
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("paymentId", event.getPaymentId());
            eventData.put("reason", event.getReason());
            eventData.put("amount", event.getAmount());
            
            eventPublisher.publishBookingCancelled(
                event.getBookingId(), 
                event.getUserId(), 
                event.getFlightNumber(),
                eventData
            );
            
            acknowledgment.acknowledge();
            log.info("Successfully processed payment failed event for booking: {}", event.getBookingId());
            
        } catch (Exception e) {
            log.error("Error processing payment failed event for booking: {}, Error: {}", 
                    event.getBookingId(), e.getMessage());
            // Don't acknowledge - message will be retried
        }
    }
}