package com.project.Service.booking.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventPublisher {
    
    private final KafkaTemplate<String, BookingEvent> kafkaTemplate;
    
    @Value("${app.events.topics.booking-created}")
    private String bookingCreatedTopic;
    
    @Value("${app.events.topics.booking-confirmed}")
    private String bookingConfirmedTopic;
    
    @Value("${app.events.topics.booking-cancelled}")
    private String bookingCancelledTopic;
    
    public void publishBookingCreated(String bookingId, Long userId, String flightNumber, Map<String, Object> eventData) {
        BookingEvent event = BookingEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(BookingEvent.BOOKING_CREATED)
                .bookingId(bookingId)
                .userId(userId)
                .flightNumber(flightNumber)
                .status("CREATED")
                .timestamp(LocalDateTime.now())
                .eventData(eventData)
                .build();
        
        publishEvent(bookingCreatedTopic, event);
    }
    
    public void publishBookingConfirmed(String bookingId, Long userId, String flightNumber, Map<String, Object> eventData) {
        BookingEvent event = BookingEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(BookingEvent.BOOKING_CONFIRMED)
                .bookingId(bookingId)
                .userId(userId)
                .flightNumber(flightNumber)
                .status("CONFIRMED")
                .timestamp(LocalDateTime.now())
                .eventData(eventData)
                .build();
        
        publishEvent(bookingConfirmedTopic, event);
    }
    
    public void publishBookingCancelled(String bookingId, Long userId, String flightNumber, Map<String, Object> eventData) {
        BookingEvent event = BookingEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(BookingEvent.BOOKING_CANCELLED)
                .bookingId(bookingId)
                .userId(userId)
                .flightNumber(flightNumber)
                .status("CANCELLED")
                .timestamp(LocalDateTime.now())
                .eventData(eventData)
                .build();
        
        publishEvent(bookingCancelledTopic, event);
    }
    
    private void publishEvent(String topic, BookingEvent event) {
        try {
            CompletableFuture<SendResult<String, BookingEvent>> future = 
                kafkaTemplate.send(topic, event.getBookingId(), event);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Event published successfully: {} to topic: {}", event.getEventType(), topic);
                } else {
                    log.error("Failed to publish event: {} to topic: {}, Error: {}", 
                            event.getEventType(), topic, ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Error publishing event: {} to topic: {}, Error: {}", 
                    event.getEventType(), topic, e.getMessage());
        }
    }
}