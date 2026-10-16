package io.github.olszewskif.fridge.identity.domain.exception;

import io.github.olszewskif.fridge.shared.domain.DomainException;

public class InvalidRefreshTokenException extends DomainException {
    public InvalidRefreshTokenException(String message) {
        super(message);
    }

    public InvalidRefreshTokenException() {
        super("This refresh accessToken is invalid!");
    }
}
