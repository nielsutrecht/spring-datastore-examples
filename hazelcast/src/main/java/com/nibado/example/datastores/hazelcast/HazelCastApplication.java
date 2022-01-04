package com.nibado.example.datastores.hazelcast;

import com.nibado.example.datastores.shared.ProductController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class HazelCastApplication {
	public static void main(String[] args) {
		SpringApplication.run(HazelCastApplication.class, args);
	}

	@Bean
	public ProductController controller(ProductCache repository) {
		return new ProductController(repository);
	}
}
