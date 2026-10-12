package io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto;

import jakarta.annotation.Nullable;

import java.util.Set;
import java.util.UUID;

public record SearchProductsRequest(
        @Nullable String search,
        @Nullable Set<UUID> categoryIds
) {
}
