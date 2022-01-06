package com.nibado.example.datastores.cassandra;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.CassandraContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

public class CassandraInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(CassandraInitializer.class);

    private static CassandraContainer CASSANDRA;

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if(System.getenv().containsKey("CI")) {
            //initializeCi(context);
        } else {
            initializeLocal(context);
        }
    }

    private void initializeLocal(ConfigurableApplicationContext context) {
        if(CASSANDRA == null) {
            CASSANDRA = new CassandraContainer<>("cassandra:3.11.10");
        }
        if(!CASSANDRA.isRunning()) {
            CASSANDRA.start();
            Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOG);
            CASSANDRA.followOutput(logConsumer);
        }

        CASSANDRA.getCluster().newSession()
                .execute("CREATE KEYSPACE IF NOT EXISTS product WITH REPLICATION = {'class':'SimpleStrategy', 'replication_factor' : 1};");

//        context.getBeanFactory().registerSingleton("cassandraContainer", CASSANDRA);
//        context.getBeanFactory().registerSingleton("cluster", CASSANDRA.getCluster());
        context.addApplicationListener((event) -> {
            if (event instanceof ContextClosedEvent) {
                CASSANDRA.stop();
            }
        });

        var contactPoint = String.format("%s:%s", CASSANDRA.getHost(), CASSANDRA.getMappedPort(9042));

        LOG.info("Cassandra active on {}", contactPoint);

        TestPropertyValues
                .of("spring.data.cassandra.contact-points:" + contactPoint)
                .applyTo(context);
    }
}
