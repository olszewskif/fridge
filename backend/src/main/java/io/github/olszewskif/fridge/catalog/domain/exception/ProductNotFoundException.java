package io.github.olszewskif.fridge.catalog.domain.exception;

import io.github.olszewskif.fridge.catalog.domain.ProductId;
import io.github.olszewskif.fridge.shared.domain.NotFoundException;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(ProductId id) {
        super("Product (ID: " + id.getValue() + ") not found");
    }
}
