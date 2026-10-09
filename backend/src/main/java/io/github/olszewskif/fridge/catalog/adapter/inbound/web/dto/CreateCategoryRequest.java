package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateCategoryRequest(
        @NotBlank String name,
        UUID parentId
) {
}
