package com.nibado.example.datastores.shared;

import java.math.BigDecimal;

public record Product(long id, String name, BigDecimal price) {
}
