package com.nibado.example.datastores.neo4j;

import com.nibado.example.datastores.sharedtests.DockerImages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.Neo4jContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

public class Neo4JInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(Neo4JInitializer.class);
    private static Neo4jContainer NEO4J;

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if(System.getenv().containsKey("CI")) {
            //initializeCi(context);
        } else {
            initializeLocal(context);
        }
    }

    private void initializeLocal(ConfigurableApplicationContext context) {
        if(NEO4J == null) {
            NEO4J = new Neo4jContainer(DockerImages.NEO4J);
        }
        if(!NEO4J.isRunning()) {
            NEO4J.start();
            Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOG);
            NEO4J.followOutput(logConsumer);
        }

        context.addApplicationListener((event) -> {
            if (event instanceof ContextClosedEvent) {
                NEO4J.stop();
            }
        });

        var uri = String.format("bolt://%s:%s", NEO4J.getContainerIpAddress(), NEO4J.getMappedPort(7687));

        TestPropertyValues
                .of("spring.neo4j.uri:" + uri)
                .applyTo(context);
    }
}
