package com.project.Service.payment.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {
    
    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;
    
    @Value("${app.events.topics.payment-initiated}")
    private String paymentInitiatedTopic;
    
    @Value("${app.events.topics.payment-completed}")
    private String paymentCompletedTopic;
    
    @Value("${app.events.topics.payment-failed}")
    private String paymentFailedTopic;
    
    @Value("${app.events.topics.payment-refunded}")
    private String paymentRefundedTopic;
    
    public void publishPaymentCompleted(String paymentId, String bookingId, Long userId, 
                                      BigDecimal amount, String currency, String transactionId, 
                                      Map<String, Object> eventData) {
        PaymentEvent event = PaymentEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(PaymentEvent.PAYMENT_COMPLETED)
                .paymentId(paymentId)
                .bookingId(bookingId)
                .userId(userId)
                .amount(amount)
                .currency(currency)
                .status("COMPLETED")
                .transactionId(transactionId)
                .timestamp(LocalDateTime.now())
                .eventData(eventData)
                .build();
        
        publishEvent(paymentCompletedTopic, event);
    }
    
    public void publishPaymentFailed(String paymentId, String bookingId, Long userId, 
                                   BigDecimal amount, String currency, String reason,
                                   Map<String, Object> eventData) {
        PaymentEvent event = PaymentEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(PaymentEvent.PAYMENT_FAILED)
                .paymentId(paymentId)
                .bookingId(bookingId)
                .userId(userId)
                .amount(amount)
                .currency(currency)
                .status("FAILED")
                .timestamp(LocalDateTime.now())
                .eventData(eventData)
                .build();
        
        publishEvent(paymentFailedTopic, event);
    }
    
    public void publishPaymentInitiated(String paymentId, String bookingId, Long userId, 
                                      BigDecimal amount, String currency, String paymentMethod,
                                      Map<String, Object> eventData) {
        PaymentEvent event = PaymentEvent.builder()
                .eventId(UUID.randomUUID().toString())
                .eventType(PaymentEvent.PAYMENT_INITIATED)
                .paymentId(paymentId)
                .bookingId(bookingId)
                .userId(userId)
                .amount(amount)
                .currency(currency)
                .status("INITIATED")
                .paymentMethod(paymentMethod)
                .timestamp(LocalDateTime.now())
                .eventData(eventData)
                .build();
        
        publishEvent(paymentInitiatedTopic, event);
    }
    
    private void publishEvent(String topic, PaymentEvent event) {
        try {
            CompletableFuture<SendResult<String, PaymentEvent>> future = 
                kafkaTemplate.send(topic, event.getPaymentId(), event);
            
            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Payment event published successfully: {} to topic: {}", event.getEventType(), topic);
                } else {
                    log.error("Failed to publish payment event: {} to topic: {}, Error: {}", 
                            event.getEventType(), topic, ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Error publishing payment event: {} to topic: {}, Error: {}", 
                    event.getEventType(), topic, e.getMessage());
        }
    }
}