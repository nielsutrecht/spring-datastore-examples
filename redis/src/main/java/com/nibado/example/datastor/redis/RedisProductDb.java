package com.nibado.example.datastor.redis;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RedisProductDb implements ProductDb {
    private static final Logger LOG = LoggerFactory.getLogger(RedisProductDb.class);

    private final RedisAtomicLong counter;
    private final RedisProductRepository repository;

    public RedisProductDb(RedisConnectionFactory factory, RedisProductRepository repository) {
        this.repository = repository;
        this.counter = new RedisAtomicLong("product-id", factory, 0);
    }

    @Override
    public Optional<Product> findById(long id) {
        return repository.findById(id).map(ProductEntity::toProduct);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream().map(ProductEntity::toProduct).toList();
    }

    @Override
    public Product create(Product product) {
        var newProduct = new ProductEntity(counter.incrementAndGet(), product.name(), product.price());

        repository.save(newProduct);

        LOG.info("Inserted product with id '{}', name '{}' and price '{}'", newProduct.id, newProduct.name, newProduct.price);

        return newProduct.toProduct();
    }

    @Override
    public void update(Product product) {
        repository.save(new ProductEntity(product));

        LOG.info("Updated product with id '{}', name '{}' and price '{}'", product.id(), product.name(), product.price());
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void delete(long id) {
        repository.deleteById(id);
    }
}
