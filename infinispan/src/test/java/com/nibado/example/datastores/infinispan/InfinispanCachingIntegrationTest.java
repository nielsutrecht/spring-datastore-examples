package com.nibado.example.datastores.infinispan;

import com.nibado.example.datastores.sharedtests.BaseCacheIntegrationTest;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = InfinispanInitializer.class)
class InfinispanCachingIntegrationTest extends BaseCacheIntegrationTest {

}