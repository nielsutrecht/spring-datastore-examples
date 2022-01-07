package com.nibado.example.datastores.sharedtests;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductConsumer;
import com.nibado.example.datastores.shared.ProductProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseTopicIntegrationTest {
    private static final List<Product> TEST_PRODUCTS = IntStream.range(0, 1000)
            .mapToObj(i -> new Product(i, "Product " + i, BigDecimal.valueOf(i))).toList();

    @Autowired
    private ProductProducer producer;

    @Autowired
    private ProductConsumer consumer;

    @Test
    public void Should_be_able_to_produce_and_consume_events() {
        TEST_PRODUCTS.forEach(producer::produce);

        await().atMost(30, SECONDS).until(() -> consumer.consumedProducts().size() == TEST_PRODUCTS.size());

        assertThat(consumer.consumedProducts()).hasSize(TEST_PRODUCTS.size());

        var expectedIds = TEST_PRODUCTS.stream().map(p -> p.id()).collect(Collectors.toSet());
        var actualIds = consumer.consumedProducts().stream().map(p -> p.id()).collect(Collectors.toSet());

        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
    }
}