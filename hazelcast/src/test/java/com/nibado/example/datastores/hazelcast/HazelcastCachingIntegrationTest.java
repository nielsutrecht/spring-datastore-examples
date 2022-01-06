package com.nibado.example.datastores.hazelcast;

import com.nibado.example.datastores.sharedtests.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class HazelcastCachingIntegrationTest extends BaseIntegrationTest {
    @Autowired
    public ProductCache cache;

    @Test
    public void Should_only_call_findById_when_needed() {
        var knownId = cache.findAll().stream().findFirst().get().id();
        var product = findById(knownId).getBody();

        var startCount = cache.findByIdCalls();
        for(int i = 0;i < 10;i++) {
            findById(knownId);
        }

        assertThat(cache.findByIdCalls()).isEqualTo(startCount);

        cache.update(product);
        findById(knownId);

        assertThat(cache.findByIdCalls()).isEqualTo(startCount + 1);
    }

    @Test
    public void Should_evict_on_delete() {
        var knownId1 = cache.findAll().get(0).id();
        var knownId2 = cache.findAll().get(1).id();
        findById(knownId1);
        findById(knownId2);

        cache.delete(knownId1);
        assertThat(cache.findById(knownId1)).isEmpty();

        cache.deleteAll();
        assertThat(cache.findById(knownId2)).isEmpty();
    }
}