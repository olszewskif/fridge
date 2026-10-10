package io.github.olszewskif.fridge.catalog.domain;

public record CategoryFilterQuery(
        String search,
        CategoryId parentId,
        Boolean isRoot
) {
}
