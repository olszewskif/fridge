package io.github.olszewskif.fridge.inventory.application.dto;


import io.github.olszewskif.fridge.inventory.domain.ProductId;

import java.util.Set;

public record ResolveProductsBatchQuery(Set<ProductId> productIds) {
}
