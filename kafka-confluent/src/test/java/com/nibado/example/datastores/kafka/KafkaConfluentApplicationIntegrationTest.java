package com.nibado.example.datastores.kafka;

import com.nibado.example.datastores.sharedtests.BaseTopicIntegrationTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

@Import(TestConfig.class)
@ContextConfiguration(initializers = KafkaInitializer.class)
class KafkaConfluentApplicationIntegrationTest extends BaseTopicIntegrationTest {
}
