package com.nibado.example.datastores.elasticsearch;

import com.nibado.example.datastores.shared.Product;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.math.BigDecimal;

@Document(indexName = "product")
public class ProductDocument {
    @Id
    Long id;
    String name;
    BigDecimal price;

    public ProductDocument() {
    }

    public ProductDocument(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product toProduct() {
        return new Product(id, name, price);
    }
}
