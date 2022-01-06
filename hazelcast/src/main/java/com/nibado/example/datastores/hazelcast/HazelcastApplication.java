package com.nibado.example.datastores.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.topic.ITopic;
import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class HazelcastApplication {
	public static void main(String[] args) {
		SpringApplication.run(HazelcastApplication.class, args);
	}

	@Bean
	public ProductController controller(ProductCache repository) {
		return new ProductController(repository);
	}

	@Bean
	public ITopic<Product> topic(HazelcastInstance hz) {
		return hz.getTopic("product-events");
	}
}
