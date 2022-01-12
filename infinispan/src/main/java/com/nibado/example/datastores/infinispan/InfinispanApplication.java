package com.nibado.example.datastores.infinispan;

import com.nibado.example.datastores.shared.ProductCache;
import com.nibado.example.datastores.shared.ProductController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class InfinispanApplication {
	public static void main(String[] args) {
		SpringApplication.run(InfinispanApplication.class, args);
	}

	@Bean
	public ProductCache productCache() {
		return new ProductCache();
	}

	@Bean
	public ProductController controller(ProductCache repository) {
		return new ProductController(repository);
	}
}
