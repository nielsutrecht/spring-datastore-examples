package com.nibado.example.datastor.redis;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RedisProductRepository extends CrudRepository<ProductEntity, Long> {
    List<ProductEntity> findAll();
}
