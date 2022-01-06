package com.nibado.example.datastores.hazelcast;

import com.hazelcast.topic.ITopic;
import com.hazelcast.topic.Message;
import com.hazelcast.topic.MessageListener;
import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

@Component
public class HazelcastProductConsumer implements ProductConsumer, MessageListener<Product> {
    private static final Logger LOG = LoggerFactory.getLogger(HazelcastProductConsumer.class);

    private final List<Product> consumedProducts = synchronizedList(new ArrayList<>());

    public HazelcastProductConsumer(ITopic<Product> topic) {
        topic.addMessageListener(this);
    }

    @Override
    public List<Product> consumedProducts() {
        return consumedProducts;
    }

    @Override
    public void onMessage(Message<Product> message) {
        LOG.info("Received product with id {}", message.getMessageObject().id());
        consumedProducts.add(message.getMessageObject());
    }
}
