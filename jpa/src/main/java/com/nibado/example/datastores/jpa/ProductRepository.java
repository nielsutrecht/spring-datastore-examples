package com.nibado.example.datastores.jpa;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository implements ProductDb {
    private static final Logger LOG = LoggerFactory.getLogger(ProductRepository.class);

    private final ProductCrudRepository crudRepository;

    public ProductRepository(NamedParameterJdbcTemplate template, ProductCrudRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public Optional<Product> findById(long id) {
        return crudRepository.findById(id).map(ProductRepository::entityToProduct);
    }

    @Override
    public List<Product> findAll() {
        return crudRepository.findAll().stream().map(ProductRepository::entityToProduct).toList();
    }

    @Override
    public Product create(Product product) {
        var entity = new ProductEntity(product.name(), product.price());
        crudRepository.save(entity);

        LOG.info("Inserted product with id '{}', name '{}' and price '{}'", entity.getId(), entity.getName(), entity.getPrice());

        return entityToProduct(entity);
    }

    @Override
    public void update(Product product) {
        var entity = new ProductEntity(product.id(), product.name(), product.price());

        crudRepository.save(entity);

        LOG.info("Updated product with id '{}', name '{}' and price '{}'", product.id(), product.name(), product.price());
    }

    @Override
    public void deleteAll() {
        crudRepository.deleteAll();
    }

    @Override
    public void delete(long id) {
        crudRepository.deleteById(id);
    }

    private static Product entityToProduct(ProductEntity e) {
        return new Product(e.getId(), e.getName(), e.getPrice());
    }
}
