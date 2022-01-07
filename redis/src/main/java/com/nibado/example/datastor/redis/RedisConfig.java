package com.nibado.example.datastor.redis;

import com.nibado.example.datastores.kafka.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.IOException;
import java.nio.ByteBuffer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory(
            @Value("${redis.host}") String host,
            @Value("${redis.port}") int port) {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Bean
    public RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, RedisConsumer consumer) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(consumer, new PatternTopic("product-events"));

        return container;
    }

    @Bean
    public RedisTemplate<Long, ProductEntity> template(RedisConnectionFactory connectionFactory, RedisSerializer<ProductEvent> eventSerializer) {
        var template = new RedisTemplate<Long, ProductEntity>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(eventSerializer);
        return template;
    }

    @Bean
    public RedisConsumer consumer(RedisSerializer<ProductEvent> eventSerializer) {
        return new RedisConsumer(eventSerializer);
    }

    @Bean
    public RedisSerializer<ProductEvent> eventSerializer() {
        return new ProductEventSerializer();
    }

    private static class ProductEventSerializer implements RedisSerializer<ProductEvent> {
        private static final Logger LOG = LoggerFactory.getLogger(RedisConsumer.class);

        @Override
        public byte[] serialize(ProductEvent event) throws SerializationException {
            try {
                return event.toByteBuffer().array();
            } catch (IOException e) {
                LOG.error("Error serializing", e);
                throw new RuntimeException(e);
            }
        }

        @Override
        public ProductEvent deserialize(byte[] bytes) throws SerializationException {
            try {
                return ProductEvent.fromByteBuffer(ByteBuffer.wrap(bytes));
            } catch (IOException e) {
                LOG.error("Error deserializing", e);
                throw new RuntimeException(e);
            }
        }
    }
}
