package io.github.olszewskif.fridge.inventory.domain.exception;

import io.github.olszewskif.fridge.shared.domain.DomainException;

public class InvalidUnitException extends DomainException {
    public InvalidUnitException(String message) {
        super(message);
    }
}
