package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.ProductId;

public record ResolveProductQuery(ProductId productId) {
}
