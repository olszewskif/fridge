package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import jakarta.validation.constraints.Size;

public record UpdateProductRequest(
        @Size(min = 1) String name,
        String brand,
        @Size(min = 1) String barcode,
        String imageUrl
) {
}
