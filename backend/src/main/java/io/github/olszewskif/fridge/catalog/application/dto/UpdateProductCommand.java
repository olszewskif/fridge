package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.NetQuantity;
import io.github.olszewskif.fridge.catalog.domain.ProductId;
import jakarta.annotation.Nullable;

public record UpdateProductCommand(
        ProductId id,
        String name,
        @Nullable String brand,
        Barcode barcode,
        @Nullable NetQuantity netQuantity,
        @Nullable String imageUrl
) {
}
