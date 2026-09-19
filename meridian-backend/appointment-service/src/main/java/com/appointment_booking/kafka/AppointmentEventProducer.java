package com.appointment_booking.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentEventProducer {

    private final KafkaTemplate<String, AppointmentBookedEvent> kafkaTemplate;

    public void publishAppointmentBooked(AppointmentBookedEvent event) {
        kafkaTemplate.send("appointment-booked-topic", event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        System.out.println("Kafka event published successfully.");
                    } else {
                        System.out.println("Failed to publish event: " + ex.getMessage());
                    }
                });
    }
}