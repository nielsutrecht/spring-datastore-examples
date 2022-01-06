package com.nibado.example.datastores.sharedtests;

import com.nibado.example.datastores.shared.Product;
import com.nibado.example.datastores.shared.ProductDb;
import com.nibado.example.datastores.shared.ProductsResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static com.nibado.example.datastores.sharedtests.TestData.PRODUCTS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {
    @Autowired
    public TestRestTemplate template;

    @Autowired
    public ProductDb db;

    @BeforeEach
    public void setup() {
        db.deleteAll();

        PRODUCTS.forEach(db::create);
    }

    @Test
    public void Should_return_404_on_missing_product() {
        var response = template.getForEntity("/product/12345678890", Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void Should_return_a_product() {
        var product = db.findAll().stream().findFirst().get();

        var response = findById(product.id());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(product);
    }

    @Test
    public void Should_return_a_list_of_products() {
        var response = template.getForEntity("/product", ProductsResponse.class);
        var names = PRODUCTS.stream().map(Product::name).toList();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().products())
                .hasSize(PRODUCTS.size())
                .extracting("name")
                .containsAll(names);
    }

    @Test
    public void Should_create_a_product() {
        db.deleteAll();

        var response = template.postForEntity("/product", new Product(0, "New Product", BigDecimal.TEN), Product.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        var first = db.findAll().stream().findFirst().orElseThrow();

        assertProduct(response.getBody(), first);
    }

    @Test
    public void Should_update_a_product() {
        var product = db.findAll().stream().findFirst().get();
        var newProduct = new Product(product.id(), "A new name", product.price().add(BigDecimal.TEN));

        template.put("/product/" + product.id(), newProduct);

        var updatedProduct = db.findById(product.id()).orElseThrow();

        assertThat(updatedProduct).isEqualTo(newProduct);
    }

    @Test
    public void Should_delete_a_product() {
        var product = db.findAll().stream().findFirst().get();

        template.delete("/product/" + product.id());

        assertThat(db.findById(product.id())).isEmpty();
    }

    protected ResponseEntity<Product> findById(long id) {
        return template.getForEntity("/product/" + id, Product.class);
    }

    protected void assertProduct(Product actual, Product expected) {
        assertThat(actual.id()).isEqualTo(expected.id());
        assertThat(actual.name()).isEqualTo(expected.name());
        assertThat(actual.price().setScale(2, RoundingMode.HALF_UP)).isEqualTo(expected.price().setScale(2, RoundingMode.HALF_UP));
    }
}
