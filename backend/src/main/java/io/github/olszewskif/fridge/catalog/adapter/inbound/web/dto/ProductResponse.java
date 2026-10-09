package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import java.util.Set;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String name,
        String brand,
        String barcode,
        String imageUrl,
        Set<CategorySummary> categories
) {
    public record CategorySummary(
            UUID id,
            String name,
            UUID parentId
    ) {}
}
