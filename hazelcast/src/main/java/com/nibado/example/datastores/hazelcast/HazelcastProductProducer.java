package com.nibado.example.datastores.hazelcast;

import com.hazelcast.topic.ITopic;
import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HazelcastProductProducer implements ProductProducer {
    private static final Logger LOG = LoggerFactory.getLogger(HazelcastProductProducer.class);

    private final ITopic<Product> topic;

    public HazelcastProductProducer(ITopic<Product> topic) {
        this.topic = topic;
    }

    @Override
    public void produce(Product product) {
        topic.publish(product);
        LOG.info("Produced product with id {}", product.id());
    }
}
