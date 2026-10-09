package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.ProductId;

public record AddCategoryCommand(
        ProductId productId,
        CategoryId categoryId
) {
}
