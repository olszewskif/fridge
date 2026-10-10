package io.github.olszewskif.fridge.inventory.domain.exception;

import io.github.olszewskif.fridge.shared.domain.DomainException;

public class InvalidAmountException extends DomainException {
    public InvalidAmountException(String message) {
        super(message);
    }
}
