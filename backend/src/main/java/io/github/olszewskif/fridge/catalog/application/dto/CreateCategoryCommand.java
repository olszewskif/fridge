package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.CategoryId;

public record CreateCategoryCommand(
        String name,
        CategoryId parentId
) {
}
