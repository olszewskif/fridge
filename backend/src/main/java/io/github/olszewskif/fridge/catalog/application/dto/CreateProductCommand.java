package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.NetQuantity;
import jakarta.annotation.Nullable;

import java.util.Set;

public record CreateProductCommand(
        String name,
        @Nullable String brand,
        Barcode barcode,
        @Nullable NetQuantity netQuantity,
        @Nullable String imageUrl,
        @Nullable Set<CategoryId> categoryIds
) {
}
