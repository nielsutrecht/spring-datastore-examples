package com.nibado.example.datastores.shared;

import java.util.List;
import java.util.Optional;

public interface ProductDb {
    Optional<Product> findById(long id);
    List<Product> findAll();
    Product create(Product product);
    void update(Product product);
    void deleteAll();
    void delete(long id);
}
