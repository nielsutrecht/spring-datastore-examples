package com.nibado.example.datastores.kafka;

import com.nibado.example.datastores.sharedtests.DockerImages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

public class KafkaInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaInitializer.class);
    private static KafkaContainer KAFKA;

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if(System.getenv().containsKey("CI")) {
            //initializeCi(context);
        } else {
            initializeLocal(context);
        }
    }

    private void initializeLocal(ConfigurableApplicationContext context) {
        if(KAFKA == null) {
            KAFKA = new KafkaContainer(DockerImages.KAFKA);
        }
        if(!KAFKA.isRunning()) {
            KAFKA.start();
            Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOG);
            KAFKA.followOutput(logConsumer);
        }

        context.getBeanFactory().registerSingleton("kafkaContainer", KAFKA);
        context.addApplicationListener((event) -> {
            if (event instanceof ContextClosedEvent) {
                KAFKA.stop();
            }
        });

        System.setProperty("spring.kafka.bootstrap-servers", KAFKA.getBootstrapServers());
        TestPropertyValues
                .of("spring.kafka.bootstrap-servers:" + KAFKA.getBootstrapServers())
                .applyTo(context);
    }
}
