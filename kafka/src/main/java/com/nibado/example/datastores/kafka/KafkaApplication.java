package com.nibado.example.datastores.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class KafkaApplication {
	public static void main(String[] args) {
		SpringApplication.run(KafkaApplication.class, args);
	}
}
