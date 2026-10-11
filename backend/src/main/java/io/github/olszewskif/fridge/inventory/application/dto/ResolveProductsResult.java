package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.ProductId;

import java.util.Set;

public record ResolveProductsResult(Set<ProductSummaryResult> products) {

    public record ProductSummaryResult(ProductId productId, String name, String brand) {}
}
