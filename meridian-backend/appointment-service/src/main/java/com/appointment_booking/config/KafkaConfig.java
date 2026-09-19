package com.appointment_booking.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic appointmentBookedTopic() {
        return TopicBuilder.name("appointment-booked-topic")
                .partitions(3)    // This allows up to 3 consumers to work in parallel
                .replicas(1)      // 1 is fine for your current Docker/Local setup
                .build();
    }
}