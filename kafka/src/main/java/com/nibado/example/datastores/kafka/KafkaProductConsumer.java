package com.nibado.example.datastores.kafka;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductConsumer;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.synchronizedList;

@Component
public class KafkaProductConsumer implements ProductConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaProductConsumer.class);

    final List<Product> consumedProducts =  synchronizedList(new ArrayList<>());

    @KafkaListener(topics = "${service.topic}", groupId = "kafka-example")
    public void connectionStatesEvent(List<ConsumerRecord<String, ProductEvent>> records, Consumer<?, ?> consumer) {
        LOG.info("Received {} records", records.size());
        var products = records.stream().map(cr -> {
            var event = cr.value();
            return new Product(event.getId(), event.getName().toString(), event.getPrice());
        }).toList();

        consumedProducts.addAll(products);
    }

    @Override
    public List<Product> consumedProducts() {
        return consumedProducts;
    }
}
