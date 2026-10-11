package io.github.olszewskif.fridge.catalog.domain;

import jakarta.annotation.Nullable;

import java.util.Set;

public record ProductFilterQuery(
        @Nullable String search,
        @Nullable Set<CategoryId> categoryIds
) {
}
