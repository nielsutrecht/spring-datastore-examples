package com.nibado.example.datastor.redis;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductProducer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisProducer implements ProductProducer {
    private final RedisTemplate<Long, ProductEntity> template;

    public RedisProducer(RedisTemplate<Long, ProductEntity> template) {
        this.template = template;
    }

    @Override
    public void produce(Product product) {
        template.convertAndSend("product-events", product.toEvent());
    }
}
