package com.nibado.example.datastores.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaConfluentApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaConfluentApplication.class, args);
	}
}
