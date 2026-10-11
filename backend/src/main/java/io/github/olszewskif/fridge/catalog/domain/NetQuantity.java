package io.github.olszewskif.fridge.catalog.domain;

import io.github.olszewskif.fridge.catalog.domain.exception.InvalidAmountException;

public record NetQuantity(double amount, Unit unit) {

    public NetQuantity {
        if (amount <= 0) {
            throw new InvalidAmountException("Quantity must be more than 0!");
        }
        if (unit == null) {
            throw new IllegalArgumentException("Unit cannot be null!");
        }
    }
}
