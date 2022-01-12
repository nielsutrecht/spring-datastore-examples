package com.nibado.example.datastores.neo4j;


import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductCrudRepository extends CrudRepository<ProductNode, Long> {
    List<ProductNode> findAll();
}
