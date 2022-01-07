package com.nibado.example.datastor.redis;

import com.nibado.example.datastores.shared.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.math.BigDecimal;

@RedisHash("products")
public class ProductEntity {
    @Id
    Long id;
    String name;
    BigDecimal price;

    ProductEntity() {
    }

    ProductEntity(Product product) {
        this(product.id(), product.name(), product.price());
    }

    ProductEntity(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(id, name, price);
    }
}