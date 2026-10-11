package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import io.github.olszewskif.fridge.catalog.domain.NetQuantity;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
        @Nullable @Size(min = 1) String name,
        @Nullable String brand,
        @Nullable @Size(min = 1) String barcode,
        @Nullable @Valid NetQuantityDto netQuantity,
        @Nullable String imageUrl
) {
}
