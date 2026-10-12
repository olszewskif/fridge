package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.CategoryId;

import java.util.Collections;
import java.util.Set;

public record CategoryResult(
        CategoryId id,
        String name,
        CategoryId parentId,
        Set<String> aliases
) {
    public CategoryResult(CategoryId id, String name, CategoryId parentId) {
        this(id, name, parentId, Collections.emptySet());
    }
}
