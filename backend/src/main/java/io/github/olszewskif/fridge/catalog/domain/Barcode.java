package io.github.olszewskif.fridge.catalog.domain;

public record Barcode(String value) {

    public Barcode {
        if(value == null || value.isBlank()) {
            throw new IllegalArgumentException("Barcode cannot be null or blank!");
        }
    }
}
