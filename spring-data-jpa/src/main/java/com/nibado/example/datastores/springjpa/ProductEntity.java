package com.nibado.example.datastores.springjpa;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    private String name;
    private BigDecimal price;
    private Instant modified;

    public ProductEntity() {
    }

    public ProductEntity(String name, BigDecimal price) {
        this(null, name, price);
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
