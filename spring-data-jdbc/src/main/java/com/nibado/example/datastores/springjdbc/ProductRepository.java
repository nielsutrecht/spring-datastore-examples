package com.nibado.example.datastores.springjdbc;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static java.util.Map.of;

@Repository
public class ProductRepository implements ProductDb {
    private static final Logger LOG = LoggerFactory.getLogger(ProductRepository.class);

    private final NamedParameterJdbcTemplate template;
    private final ProductCrudRepository crudRepository;

    public ProductRepository(NamedParameterJdbcTemplate template, ProductCrudRepository crudRepository) {
        this.template = template;
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

        LOG.info("Inserted product with id '{}', name '{}' and price '{}'", entity.id, entity.name, entity.price);

        return entityToProduct(entity);
    }

    @Override
    public void update(Product product) {
        template.update("UPDATE  product SET name = :name, price = :price, modified = NOW() WHERE id = :id",
                of("name", product.name(), "price", product.price(), "id", product.id()));

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
        return new Product(e.id, e.name, e.price);
    }
}
