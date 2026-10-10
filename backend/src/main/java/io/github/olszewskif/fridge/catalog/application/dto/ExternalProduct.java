package io.github.olszewskif.fridge.catalog.application.dto;

import io.github.olszewskif.fridge.catalog.domain.Barcode;
import jakarta.annotation.Nullable;

import java.util.Set;

public record ExternalProduct(
        String name,
        @Nullable String brand,
        Barcode barcode,
        @Nullable String imageUrl,
        Set<String> categories
) {
}
