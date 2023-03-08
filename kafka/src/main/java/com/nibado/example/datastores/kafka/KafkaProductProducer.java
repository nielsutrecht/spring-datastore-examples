package com.nibado.example.datastores.kafka;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProductProducer implements ProductProducer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaProductProducer.class);

    private final KafkaTemplate<String, ProductEvent> template;
    private final String topic;

    public KafkaProductProducer(KafkaTemplate<String, ProductEvent> template, @Value("${service.topic}") String topic) {
        this.template = template;
        this.topic = topic;
    }

    @Override
    public void produce(Product product) {
        var future = template.send(topic, product.toEvent());

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                LOG.error("Error producing event", ex);
            } else {
                var id = result.getProducerRecord().value().getId();
                LOG.info("Published event for product {}", id);
            }
        });
    }
}
