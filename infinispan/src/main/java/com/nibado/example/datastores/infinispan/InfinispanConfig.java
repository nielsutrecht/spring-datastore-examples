package com.nibado.example.datastores.infinispan;

import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.configuration.cache.CacheMode;
import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfinispanConfig {
    public InfinispanConfig(RemoteCacheManager cacheManager) {
        var cfg = new ConfigurationBuilder()
                .clustering()
                .cacheMode(CacheMode.LOCAL)
                .build();

        cacheManager.administration().createCache("product", cfg);
    }
}
