package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.inventory.domain.exception.InvalidQuantityException;

public record Quantity(int value) {

    public Quantity {
        if (value <= 0) {
            throw new InvalidQuantityException("Quantity muse be more than 0!");
        }
    }
}
