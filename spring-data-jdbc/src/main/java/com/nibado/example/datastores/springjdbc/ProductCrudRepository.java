package com.nibado.example.datastores.springjdbc;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductCrudRepository extends CrudRepository<ProductEntity, Long> {
    List<ProductEntity> findAll();
}
