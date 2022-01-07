package com.nibado.example.datastores.elasticsearch;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ElasticSearchProductDb implements ProductDb {
    private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchProductDb.class);

    private final ElasticSearchProductRepository repository;
    private final AtomicLong nextId = new AtomicLong();

    public ElasticSearchProductDb(ElasticSearchProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Product> findById(long id) {
        return repository.findById(id).map(ProductDocument::toProduct);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll().stream().map(ProductDocument::toProduct).toList();
    }

    @Override
    public Product create(Product product) {
        var newDocument = new ProductDocument(nextId.incrementAndGet(), product.name(), product.price());
        repository.save(newDocument);
        LOG.info("Inserted product with id '{}', name '{}' and price '{}'", newDocument.id, newDocument.name, newDocument.price);
        return newDocument.toProduct();
    }

    @Override
    public void update(Product product) {
        var newDocument = new ProductDocument(product.id(), product.name(), product.price());
        repository.save(newDocument);
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
