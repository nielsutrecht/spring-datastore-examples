package com.nibado.example.datastor.redis;

import com.nibado.example.datastores.shared.ProductController;
import com.nibado.example.datastores.shared.ProductDb;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisApplication {
	public static void main(String[] args) {
		SpringApplication.run(RedisApplication.class, args);
	}

	@Bean
	public ProductController controller(ProductDb productDb) {
		return new ProductController(productDb);
	}
}
