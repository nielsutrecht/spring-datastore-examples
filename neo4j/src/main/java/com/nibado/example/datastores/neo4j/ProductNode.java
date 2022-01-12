package com.nibado.example.datastores.neo4j;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.math.BigDecimal;
import java.time.Instant;

@Node("Product")
public class ProductNode {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private BigDecimal price;
    private Instant modified;

    public ProductNode() {
    }

    public ProductNode(String name, BigDecimal price) {
        this(null, name, price);
    }

    public ProductNode(Long id, String name, BigDecimal price) {
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
