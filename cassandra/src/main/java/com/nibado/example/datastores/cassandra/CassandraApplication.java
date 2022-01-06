package com.nibado.example.datastores.cassandra;

import com.nibado.example.datastores.shared.ProductController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@SpringBootApplication
@EnableCassandraRepositories(basePackages = { "com.nibado.example.datastores.cassandra" })
public class CassandraApplication {
	public static void main(String[] args) {
		SpringApplication.run(CassandraApplication.class, args);
	}

	@Bean
	public ProductController controller(ProductRepository repository) {
		return new ProductController(repository);
	}


}
