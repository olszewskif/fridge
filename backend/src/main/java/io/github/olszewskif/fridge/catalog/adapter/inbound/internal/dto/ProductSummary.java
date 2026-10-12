package io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto;

import jakarta.annotation.Nullable;

import java.util.Set;
import java.util.UUID;

public record ProductSummary(
        UUID productId,
        String name,
        @Nullable String brand,
        String barcode,
        @Nullable String imageUrl,
        @Nullable NetQuantityDto netQuantity,
        @Nullable Set<CategorySummary> categories
) {
    public record NetQuantityDto(double amount, String unit) {}
    public record CategorySummary(UUID id, String name) {}
}

