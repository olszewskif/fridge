package io.github.olszewskif.fridge.inventory.application.dto;

import java.util.Set;

public record SearchProductsResult(Set<ProductSummaryResult> products) { }
