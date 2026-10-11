package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.Set;
import java.util.UUID;

public record CreateProductRequest(
        @NotBlank String name,
        @Nullable String brand,
        @NotBlank String barcode,
        @Nullable @Valid NetQuantityDto netQuantity,
        @Nullable String imageUrl,
        @Nullable Set<UUID> categoryIds
) {
}
