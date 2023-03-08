package com.nibado.example.datastores.infinispan;

import com.nibado.example.datastores.sharedtests.DockerImages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.util.Map;

public class InfinispanInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(InfinispanInitializer.class);

    private static GenericContainer INFINISPAN;

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if(System.getenv().containsKey("CI")) {
            //initializeCi(context);
        } else {
            initializeLocal(context);
        }
    }

    private void initializeLocal(ConfigurableApplicationContext context) {
        var envVars = Map.of("USER", "user", "PASS", "pass");
        if(INFINISPAN == null) {
            INFINISPAN = new GenericContainer<>(DockerImages.INFINISPAN)
                    .withEnv(envVars)
                    .withExposedPorts(11222);
        }
        if(!INFINISPAN.isRunning()) {
            INFINISPAN.start();
            Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOG);
            INFINISPAN.followOutput(logConsumer);
        }

        context.addApplicationListener((event) -> {
            if (event instanceof ContextClosedEvent) {
                INFINISPAN.stop();
            }
        });

        var contactPoint = String.format("%s:%s", "127.0.0.1", INFINISPAN.getMappedPort(11222));

        LOG.info("Infinispan active on {}", contactPoint);

        TestPropertyValues
                .of("infinispan.remote.server-list:" + contactPoint)
                .applyTo(context);
    }
}
