package com.nibado.example.datastores.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {
    @Bean
    public NewTopic topic(@Value("${service.topic}") String topic) {
        return new NewTopic(topic, 2, (short)1);
    }
}
