package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.Alias;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;

import java.util.Collections;
import java.util.Set;

public record CreateCategoryCommand(
        String name,
        CategoryId parentId,
        Set<Alias> aliases
) {
    public CreateCategoryCommand(String name, CategoryId parentId) {
        this(name, parentId, Collections.emptySet());
    }
}
