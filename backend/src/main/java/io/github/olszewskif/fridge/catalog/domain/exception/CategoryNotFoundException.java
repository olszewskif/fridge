package io.github.olszewskif.fridge.catalog.domain.exception;

import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.shared.domain.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(CategoryId id) {
        super("Category (ID: " + id.getValue() + ") not found");
    }
}
