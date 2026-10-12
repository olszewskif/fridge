package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public record CreateCategoryRequest(
        @NotBlank String name,
        UUID parentId,
        Set<String> aliases
) {
    public CreateCategoryRequest(String name, UUID parentId) {
        this(name, parentId, Collections.emptySet());
    }
}
