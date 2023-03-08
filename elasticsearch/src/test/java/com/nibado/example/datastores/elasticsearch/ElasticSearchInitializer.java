package com.nibado.example.datastores.elasticsearch;

import com.nibado.example.datastores.sharedtests.DockerImages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

public class ElasticSearchInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchInitializer.class);

    private static ElasticsearchContainer ELASTIC;

    @Override
    public void initialize(ConfigurableApplicationContext context) {
        if(System.getenv().containsKey("CI")) {
            //initializeCi(context);
        } else {
            initializeLocal(context);
        }
    }

    private void initializeLocal(ConfigurableApplicationContext context) {
        if(ELASTIC == null) {
            ELASTIC = new ElasticsearchContainer(DockerImages.ELASTIC_SEARCH);
        }

        if(!ELASTIC.isRunning()) {
            ELASTIC.start();
            Slf4jLogConsumer logConsumer = new Slf4jLogConsumer(LOG);
            ELASTIC.followOutput(logConsumer);
        }

        context.addApplicationListener((event) -> {
            if (event instanceof ContextClosedEvent) {
                ELASTIC.stop();
            }
        });

        TestPropertyValues
                .of("spring.elasticsearch.uris:" + ELASTIC.getHttpHostAddress())
                .applyTo(context);
    }
}
