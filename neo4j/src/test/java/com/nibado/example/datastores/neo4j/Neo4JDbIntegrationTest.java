package com.nibado.example.datastores.neo4j;

import com.nibado.example.datastores.sharedtests.BaseDbIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = Neo4JInitializer.class)
class Neo4JDbIntegrationTest extends BaseDbIntegrationTest {

}