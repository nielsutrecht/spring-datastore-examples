package com.nibado.example.datastor.redis;

import com.nibado.example.datastores.sharedtests.BaseTopicIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = RedisInitializer.class)
class RedisTopicIntegrationTest extends BaseTopicIntegrationTest {
}