package com.nibado.example.datastores.shared;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ProductCache implements ProductDb {
    private static final Logger LOG = LoggerFactory.getLogger(ProductCache.class);
    private final AtomicLong nextId = new AtomicLong(0);
    private final List<Product> data = new ArrayList<>();
    private final AtomicLong findByIdCalls = new AtomicLong(0);

    @Cacheable("product")
    @Override
    public Optional<Product> findById(long id) {
        findByIdCalls.incrementAndGet();
        return data.stream().filter(p -> p.id() == id).findFirst();
    }

    @Override
    public List<Product> findAll() {
        return data;
    }

    @Override
    public Product create(Product product) {
        var newProduct = new Product(nextId.incrementAndGet(), product.name(), product.price());

        data.add(newProduct);

        LOG.info("Inserted product with id '{}', name '{}' and price '{}'", newProduct.id(), newProduct.name(), newProduct.price());

        return newProduct;
    }

    @CacheEvict(value = "product", key = "#product.id")
    @Override
    public void update(Product product) {
        delete(product.id());
        data.add(product);

        LOG.info("Updated product with id '{}', name '{}' and price '{}'", product.id(), product.name(), product.price());
    }

    @CacheEvict(value = "product", allEntries = true)
    @Override
    public void deleteAll() {
        data.clear();
    }

    @CacheEvict(value = "product", key = "#id")
    @Override
    public void delete(long id) {
        data.removeIf(p -> p.id() == id);
    }

    public long findByIdCalls() {
        return findByIdCalls.get();
    }
}
