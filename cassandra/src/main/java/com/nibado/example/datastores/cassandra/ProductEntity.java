package com.nibado.example.datastores.cassandra;

import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@Table("product")
public class ProductEntity {
    private static AtomicLong NEXT_ID = new AtomicLong();
    @Id
    private Long id;

    private String name;
    private BigDecimal price;
    private Instant modified;

    public ProductEntity() {
    }

    public ProductEntity(String name, BigDecimal price) {
        this(NEXT_ID.incrementAndGet(), name, price);
    }

    public ProductEntity(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.modified = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Instant getModified() {
        return modified;
    }
}
