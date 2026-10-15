package io.github.olszewskif.fridge.identity.application.exception;

import io.github.olszewskif.fridge.shared.domain.DomainException;

public class InvalidCredentialsException extends DomainException {
    public InvalidCredentialsException() {
        super("Invalid email for password!");
    }
}
