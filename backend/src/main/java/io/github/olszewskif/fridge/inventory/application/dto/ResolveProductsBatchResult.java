package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.ProductId;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record ResolveProductsBatchResult(Set<ProductSummaryResult> products) {

    public Map<ProductId, ProductSummaryResult> asMap() {
        if (products == null) return Collections.emptyMap();
        return products.stream()
                .collect(Collectors.toMap(ProductSummaryResult::productId, p -> p, (a, b) -> a));
    }
}
