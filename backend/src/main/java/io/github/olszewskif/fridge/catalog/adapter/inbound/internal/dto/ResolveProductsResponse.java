package io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto;

import java.util.Set;
import java.util.UUID;

public record ResolveProductsResponse(Set<ProductSummary> products) {

    public record ProductSummary(UUID productId, String name, String brand) {}
}
