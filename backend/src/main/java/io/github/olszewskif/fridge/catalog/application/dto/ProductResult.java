package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.ProductId;

import java.util.Set;

public record ProductResult(
        ProductId id,
        String name,
        String brand,
        Barcode barcode,
        String imageUrl,
        Set<CategorySummary> categories
) {
    public record CategorySummary(
            CategoryId id,
            String name,
            CategoryId parentId
    ) {
    }
}
