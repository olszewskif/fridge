package io.github.olszewskif.fridge.identity.domain.exception;

import io.github.olszewskif.fridge.identity.domain.RefreshTokenId;
import io.github.olszewskif.fridge.shared.domain.DomainException;

public class RefreshTokenAlreadyRevokedException extends DomainException {
    public RefreshTokenAlreadyRevokedException(RefreshTokenId id) {
        super("Refresh accessToken " + id.getValue() + " has already been revoked!");
    }
}
