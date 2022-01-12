package com.nibado.example.datastores.dynamodb;

import com.nibado.example.datastores.sharedtests.BaseDbIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = DynamoDbInitializer.class)
class DynamoDbIntegrationTest extends BaseDbIntegrationTest {

}