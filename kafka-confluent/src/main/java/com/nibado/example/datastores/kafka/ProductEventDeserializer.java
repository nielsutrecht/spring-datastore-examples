package com.nibado.example.datastores.kafka;

import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;

public class ProductEventDeserializer implements Deserializer<ProductEvent> {
    @Override
    public ProductEvent deserialize(String s, byte[] bytes) {
        try {
            return ProductEvent.getDecoder().decode(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
