package com.nibado.example.datastores.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;

import java.io.File;

public class KafkaInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaInitializer.class);
    private static DockerComposeContainer DOCKER_COMPOSE;

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if(System.getenv().containsKey("CI")) {
            //initializeCi(context);
        } else {
            initializeLocal(context);
        }
    }

    private void initializeLocal(ConfigurableApplicationContext context) {

        if(DOCKER_COMPOSE == null) {
            DOCKER_COMPOSE = new DockerComposeContainer(new File("src/test/resources/confluent-compose.yml"))
                .withExposedService("broker", 9092)
                .withExposedService("schema-registry", 8081);
        }

        if(DOCKER_COMPOSE.getContainerByServiceName("broker").isEmpty()) {
            DOCKER_COMPOSE.start();
        }

        var kafka = (ContainerState) DOCKER_COMPOSE.getContainerByServiceName("broker").orElseThrow();

        context.getBeanFactory().registerSingleton("kafkaContainer", kafka);
        context.addApplicationListener((event) -> {
            if (event instanceof ContextClosedEvent) {
                DOCKER_COMPOSE.stop();
            }
        });

        var brokers = String.format("%s:%s",
            DOCKER_COMPOSE.getServiceHost("broker", 9092),
            DOCKER_COMPOSE.getServicePort("broker", 9092));

        System.setProperty("spring.kafka.bootstrap-servers", brokers);
        TestPropertyValues
                .of("spring.kafka.bootstrap-servers:" + brokers)
                .applyTo(context);
    }
}
