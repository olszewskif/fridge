package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.CategoryId;
import jakarta.annotation.Nullable;

import java.util.Set;

public record SearchProductsQuery(
        @Nullable String search,
        @Nullable Set<CategoryId> categoryIds
) {
}
