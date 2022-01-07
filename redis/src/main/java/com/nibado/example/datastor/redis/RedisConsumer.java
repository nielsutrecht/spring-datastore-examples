package com.nibado.example.datastor.redis;

import com.nibado.example.datastores.kafka.ProductEvent;
import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RedisConsumer implements ProductConsumer, MessageListener {
    private static final Logger LOG = LoggerFactory.getLogger(RedisConsumer.class);

    private final List<Product> consumedProducts = Collections.synchronizedList(new ArrayList<>());
    private final RedisSerializer<ProductEvent> eventSerializer;

    public RedisConsumer(RedisSerializer<ProductEvent> eventSerializer) {
        this.eventSerializer = eventSerializer;
    }

    @Override
    public List<Product> consumedProducts() {
        return consumedProducts;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        var event = eventSerializer.deserialize(message.getBody());
        consumedProducts.add(Product.of(event));
    }
}
