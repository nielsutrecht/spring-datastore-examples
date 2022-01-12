package com.nibado.example.datastores.dynamodb;

import com.nibado.example.datastores.sharedtests.DockerImages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;

public class DynamoDbInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(DynamoDbInitializer.class);
    private static GenericContainer DYNAMO;

    private String endPoint;

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if(System.getenv().containsKey("CI")) {
            //initializeCi(context);
        } else {
            initializeLocal(context);
        }
    }

    private void initializeLocal(ConfigurableApplicationContext context) {

        if(DYNAMO == null) {
            DYNAMO = new GenericContainer(DockerImages.DYNAMODB).withExposedPorts(8000).waitingFor(
                    Wait.forListeningPort()
                    //Wait.forLogMessage(".*Ready to accept connections.*\\n", 1)
            );
        }
        if(!DYNAMO.isRunning()) {
            DYNAMO.start();
            Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOG);
            DYNAMO.followOutput(logConsumer);
        }

        context.addApplicationListener((event) -> {
            if (event instanceof ContextClosedEvent) {
                DYNAMO.stop();
            }
        });

        endPoint = String.format("http://%s:%s", DYNAMO.getContainerIpAddress(), DYNAMO.getMappedPort(8000));

        TestPropertyValues
                .of("dynamodb.endpoint:" + endPoint)
                .applyTo(context);
    }
}
