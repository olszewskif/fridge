package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.Alias;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;

import java.util.Set;

public record UpdateCategoryCommand(
        String name,
        CategoryId parentId,
        Set<Alias> aliases
) {
    public UpdateCategoryCommand(String name, CategoryId parentId) {
        this(name, parentId, null);
    }
}
