package com.nibado.example.datastor.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

public class RedisInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(RedisInitializer.class);
    private static GenericContainer REDIS;

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if(System.getenv().containsKey("CI")) {
            //initializeCi(context);
        } else {
            initializeLocal(context);
        }
    }

    private void initializeLocal(ConfigurableApplicationContext context) {
        if(REDIS == null) {
            REDIS = new GenericContainer(DockerImageName.parse("redis:6.2.6-alpine")).withExposedPorts(6379);
        }
        if(!REDIS.isRunning()) {
            REDIS.start();
            Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOG);
            REDIS.followOutput(logConsumer);
        }

        context.getBeanFactory().registerSingleton("redisContainer", REDIS);
        context.addApplicationListener((event) -> {
            if (event instanceof ContextClosedEvent) {
                REDIS.stop();
            }
        });

        var port = REDIS.getMappedPort(6379);

        System.setProperty("redis.port", port.toString());
        TestPropertyValues
                .of("redis.port:" + port)
                .applyTo(context);
    }
}
