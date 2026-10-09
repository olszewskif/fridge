package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Set;
import java.util.UUID;

public record CreateProductRequest(
        @NotBlank String name,
        String brand,
        @NotBlank String barcode,
        String imageUrl,
        Set<UUID> categoryIds
) {
}
