package com.nibado.example.datastores.dynamodb;

import com.nibado.example.datastores.shared.Product;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@DynamoDbBean
public class ProductEntity {
    public static final String TABLE = "product";
    private static final AtomicLong NEXT_ID = new AtomicLong();

    private Long id;
    private String name;
    private String price;

    public ProductEntity() {
    }

    public ProductEntity(String name, BigDecimal price) {
        this(NEXT_ID.incrementAndGet(), name, price);
    }

    public ProductEntity(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price.toString();
    }

    @DynamoDbPartitionKey
    @DynamoDbAttribute("id")
    public Long getId() {
        return id;
    }

    @DynamoDbAttribute("name")
    public String getName() {
        return name;
    }

    @DynamoDbAttribute("price")
    public String getPrice() {
        return price;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Product toProduct() {
        return new Product(id, name, new BigDecimal(price));
    }
}
