package com.nibado.example.datastores.cassandra;

import com.nibado.example.datastores.sharedtests.BaseDbIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = CassandraInitializer.class)

class CassandraApplicationTest extends BaseDbIntegrationTest {

}