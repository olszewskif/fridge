package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.ProductId;

public record UpdateProductCommand(
        ProductId id,
        String name,
        String brand,
        Barcode barcode,
        String imageUrl
) {
}
