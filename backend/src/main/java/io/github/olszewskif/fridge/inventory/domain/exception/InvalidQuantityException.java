package io.github.olszewskif.fridge.inventory.domain.exception;

import io.github.olszewskif.fridge.shared.domain.DomainException;

public class InvalidQuantityException extends DomainException {
    public InvalidQuantityException(String message) {
        super(message);
    }
}
