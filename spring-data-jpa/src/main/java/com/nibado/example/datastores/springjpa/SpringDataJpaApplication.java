package com.nibado.example.datastores.springjpa;

import com.nibado.example.datastores.shared.ProductController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringDataJpaApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaApplication.class, args);
	}

	@Bean
	public ProductController controller(ProductRepository repository) {
		return new ProductController(repository);
	}
}
