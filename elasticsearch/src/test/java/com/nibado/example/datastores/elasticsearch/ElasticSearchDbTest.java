package com.nibado.example.datastores.elasticsearch;

import com.nibado.example.datastores.sharedtests.BaseDbIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = ElasticSearchInitializer.class)
class ElasticSearchDbTest extends BaseDbIntegrationTest {
}