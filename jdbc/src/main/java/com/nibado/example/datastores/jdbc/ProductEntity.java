package com.nibado.example.datastores.jdbc;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@Table("product")
public class ProductEntity {
    @Id
    Long id;

    String name;
    BigDecimal price;
    Instant created;
    Instant modified;

    public ProductEntity(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
        this.created = Instant.now();
        this.modified = Instant.now();
    }
}
