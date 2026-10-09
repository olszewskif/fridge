package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.CategoryId;

public record CategoryResult(
        CategoryId id,
        String name,
        CategoryId parentId
) {
}
