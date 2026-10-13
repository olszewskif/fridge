package io.github.olszewskif.fridge.identity.domain.exception;

import io.github.olszewskif.fridge.shared.domain.DomainException;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException(String email) {
        super("Email " + email + " is invalid.");
    }
}
