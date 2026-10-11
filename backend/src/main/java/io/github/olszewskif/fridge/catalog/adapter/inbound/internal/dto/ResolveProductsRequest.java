package io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto;

import io.github.olszewskif.fridge.shared.domain.PageQuery;
import jakarta.annotation.Nullable;

import java.util.Set;
import java.util.UUID;

public record ResolveProductsRequest(
        @Nullable String search,
        @Nullable Set<UUID> categoryIds
) {
}
