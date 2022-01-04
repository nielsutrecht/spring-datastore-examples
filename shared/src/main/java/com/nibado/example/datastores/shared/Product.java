package com.nibado.example.datastores.shared;

import java.io.Serializable;
import java.math.BigDecimal;

public record Product(long id, String name, BigDecimal price) implements Serializable {
}
