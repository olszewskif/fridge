package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import jakarta.annotation.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

public record FridgeItemResponse(
        UUID id,
        UUID productId,
        UUID addedBy,
        Instant addedAt,
        int quantity,
        @Nullable LocalDate expiryDate,
        @Nullable ProductSummaryDto product
) {
    public record ProductSummaryDto(
            UUID productId,
            String name,
            @Nullable String brand,
            String barcode,
            @Nullable String imageUrl,
            @Nullable NetQuantityDto netQuantity,
            @Nullable Set<CategorySummaryDto> categories
    ) {
        public record NetQuantityDto(double value, String unit) {}
        public record CategorySummaryDto(UUID id, String name) {}
    }
}
