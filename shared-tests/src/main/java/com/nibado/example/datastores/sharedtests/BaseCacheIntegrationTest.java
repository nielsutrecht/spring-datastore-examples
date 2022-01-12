package com.nibado.example.datastores.sharedtests;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static com.nibado.example.datastores.sharedtests.TestData.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseCacheIntegrationTest {
    @Autowired
    public TestRestTemplate template;

    @Autowired
    public ProductCache cache;

    @BeforeEach
    public void setup() {
        cache.deleteAll();

        PRODUCTS.forEach(cache::create);
    }

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

    protected ResponseEntity<Product> findById(long id) {
        return template.getForEntity("/product/" + id, Product.class);
    }
}
