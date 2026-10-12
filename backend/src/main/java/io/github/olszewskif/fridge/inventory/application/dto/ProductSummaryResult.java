package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.CategoryId;
import io.github.olszewskif.fridge.inventory.domain.ProductId;
import jakarta.annotation.Nullable;

import java.util.Set;

public record ProductSummaryResult(
        ProductId productId,
        String name,
        @Nullable String brand,
        String barcode,
        @Nullable String imageUrl,
        @Nullable NetQuantityDto netQuantityDto,
        @Nullable Set<CategorySummaryResult> categories
) {

    public record NetQuantityDto(double value, String unit) {}
    public record CategorySummaryResult(CategoryId id, String name) {}
}
