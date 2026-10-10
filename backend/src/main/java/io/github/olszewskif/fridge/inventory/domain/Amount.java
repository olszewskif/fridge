package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.inventory.domain.exception.InvalidAmountException;

public record Amount(int quantity, Unit unit) {

    public Amount {
        if (quantity <= 0) {
            throw new InvalidAmountException("Quantity must be more than 0!");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null!");
        }
    }
}
