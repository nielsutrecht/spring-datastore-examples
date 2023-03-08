package com.nibado.example.datastores.elasticsearch;

import com.nibado.example.datastores.sharedtests.BaseDbIntegrationTest;
import org.junit.jupiter.api.Disabled;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = ElasticSearchInitializer.class)
@Disabled //TODO: Fix SSL issue
class ElasticSearchDbTest extends BaseDbIntegrationTest {
}
