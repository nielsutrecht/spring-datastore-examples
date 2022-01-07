package com.nibado.example.datastores.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticSearchProductRepository extends ElasticsearchRepository<ProductDocument, Long> {
    List<ProductDocument> findAll();
}
