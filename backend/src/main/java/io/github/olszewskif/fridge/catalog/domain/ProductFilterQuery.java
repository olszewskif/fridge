package io.github.olszewskif.fridge.catalog.domain;

import io.github.olszewskif.fridge.shared.domain.PageQuery;

import java.util.Set;

public record ProductFilterQuery(
        String search,
        Set<CategoryId> categoryIds
) {
}
