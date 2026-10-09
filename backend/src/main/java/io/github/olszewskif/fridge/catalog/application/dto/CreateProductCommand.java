package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.Barcode;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;

import java.util.Set;

public record CreateProductCommand(
        String name,
        String brand,
        Barcode barcode,
        String imageUrl,
        Set<CategoryId> categoryIds
) {
}
