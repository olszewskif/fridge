package io.github.olszewskif.fridge.inventory.domain;

import java.time.LocalDate;

public record ExpiryDate(LocalDate value) {

    public ExpiryDate {
        if (value == null) {
            throw new IllegalArgumentException("Expiry date cannot be null!");
        }
    }
}
