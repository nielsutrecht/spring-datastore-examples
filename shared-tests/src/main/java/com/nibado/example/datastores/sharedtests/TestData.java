package com.nibado.example.datastores.sharedtests;

import com.nibado.example.datastores.shared.Product;

import java.math.BigDecimal;
import java.util.List;

public class TestData {
    public static final List<Product> PRODUCTS = List.of(
            new Product(0, "Espresso", BigDecimal.valueOf(3.50)),
            new Product(0, "Double Espresso", BigDecimal.valueOf(6.50)),
            new Product(0, "Americano", BigDecimal.valueOf(5.00)),
            new Product(0, "Medium Latte", BigDecimal.valueOf(5.50)),
            new Product(0, "Medium Cappuccino", BigDecimal.valueOf(5.70)),
            new Product(0, "Large Latte", BigDecimal.valueOf(5.50)),
            new Product(0, "Large Cappuccino", BigDecimal.valueOf(5.70))
    );
}
