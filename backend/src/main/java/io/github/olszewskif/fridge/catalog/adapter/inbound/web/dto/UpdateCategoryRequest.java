package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import jakarta.validation.constraints.Size;

import java.util.UUID;

public record UpdateCategoryRequest(
        @Size(min = 1) String name,
        UUID parentId
) {
}
