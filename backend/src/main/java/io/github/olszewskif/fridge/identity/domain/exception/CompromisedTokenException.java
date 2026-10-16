package io.github.olszewskif.fridge.identity.domain.exception;

import io.github.olszewskif.fridge.shared.domain.DomainException;

public class CompromisedTokenException extends DomainException {
    public CompromisedTokenException(String message) {
        super(message);
    }
}
