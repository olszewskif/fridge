package io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto;

import java.util.Set;

public record ResolveProductsBatchResponse(Set<ProductSummary> products) {
}
