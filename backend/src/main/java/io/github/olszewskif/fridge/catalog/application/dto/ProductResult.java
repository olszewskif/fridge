package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.NetQuantity;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import jakarta.annotation.Nullable;

import java.util.Set;

public record ProductResult(
        ProductId id,
        String name,
        @Nullable String brand,
        Barcode barcode,
        @Nullable NetQuantity netQuantity,
        @Nullable String imageUrl,
        @Nullable Set<CategorySummary> categories
) {
    public record CategorySummary(
            CategoryId id,
            String name,
            CategoryId parentId
    ) {
    }
}
