package com.nibado.example.datastores.shared;

import com.nibado.example.datastores.kafka.ProductEvent;

import java.io.Serializable;
import java.math.BigDecimal;

public record Product(long id, String name, BigDecimal price) implements Serializable {
    public static Product of(ProductEvent event) {
        return new Product(event.getId(), event.getName().toString(), event.getPrice());
    }

    public ProductEvent toEvent() {
        return new ProductEvent(id, name, price);
    }
}
