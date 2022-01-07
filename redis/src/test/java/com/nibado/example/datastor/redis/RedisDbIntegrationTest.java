package com.nibado.example.datastor.redis;

import com.nibado.example.datastores.sharedtests.BaseDbIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = RedisInitializer.class)
class RedisDbIntegrationTest extends BaseDbIntegrationTest {
}