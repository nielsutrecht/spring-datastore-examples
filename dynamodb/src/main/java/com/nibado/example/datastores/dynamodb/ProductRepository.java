package com.nibado.example.datastores.dynamodb;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductDb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepository implements ProductDb {
    private static final Logger LOG = LoggerFactory.getLogger(ProductRepository.class);

    private final DynamoDbTable<ProductEntity> productTable;

    public ProductRepository(DynamoDbTable<ProductEntity> productTable) {
        this.productTable = productTable;
    }

    @Override
    public Optional<Product> findById(long id) {
        var product = productTable.getItem(Key.builder().partitionValue(id).build());
        return Optional.ofNullable(product).map(ProductEntity::toProduct);
    }

    @Override
    public List<Product> findAll() {
        return productTable.scan().stream().flatMap(page -> page.items().stream())
                .map(ProductEntity::toProduct).toList();
    }

    @Override
    public Product create(Product product) {
        var entity = new ProductEntity(product.name(), product.price());

        productTable.putItem(entity);

        LOG.info("Inserted product with id '{}', name '{}' and price '{}'", entity.getId(), entity.getName(), entity.getPrice());

        return entityToProduct(entity);
    }

    @Override
    public void update(Product product) {
        var entity = new ProductEntity(product.id(), product.name(), product.price());

        productTable.putItem(entity);

        LOG.info("Updated product with id '{}', name '{}' and price '{}'", product.id(), product.name(), product.price());
    }

    @Override
    public void deleteAll() {
        findAll().stream().map(Product::id).forEach(this::delete);
    }

    @Override
    public void delete(long id) {
        productTable.deleteItem(Key.builder().partitionValue(id).build());
    }

    private static Product entityToProduct(ProductEntity e) {
        return new Product(e.getId(), e.getName(), new BigDecimal(e.getPrice()));
    }
}
