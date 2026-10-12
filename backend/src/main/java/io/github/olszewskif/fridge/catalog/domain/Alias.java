package io.github.olszewskif.fridge.catalog.domain;

import java.util.Locale;

public record Alias(String value) {

    public Alias {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Alias cannot be null or blank!");
        }
        value = value.trim().toLowerCase(Locale.ROOT);
    }

    public static Alias of(String value) {
        return new Alias(value);
    }
}
