package com.nibado.example.datastores.shared;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductDb db;

    public ProductController(ProductDb db) {
        this.db = db;
    }

    @GetMapping
    public ProductsResponse getAll() {
        return new ProductsResponse(db.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable long id) {
        return ResponseEntity.of(db.findById(id));
    }

    @PostMapping
    public ResponseEntity<Product> create(@RequestBody Product product) {
        return ResponseEntity.ok(db.create(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable long id, @RequestBody Product product) {
        if(id != product.id()) {
            throw new IllegalArgumentException("ID should match Product ID");
        }
        db.update(product);
        return ResponseEntity.accepted().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable long id) {
        db.delete(id);
        return ResponseEntity.accepted().build();
    }
}
