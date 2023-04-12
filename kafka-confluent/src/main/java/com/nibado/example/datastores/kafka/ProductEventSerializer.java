package com.nibado.example.datastores.kafka;

import org.apache.kafka.common.serialization.Serializer;

import java.io.IOException;

public class ProductEventSerializer implements Serializer<ProductEvent> {
    @Override
    public byte[] serialize(String s, ProductEvent event) {
        try {
            return ProductEvent.getEncoder().encode(event).array();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
