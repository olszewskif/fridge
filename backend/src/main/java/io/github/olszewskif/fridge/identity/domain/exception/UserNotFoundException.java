package io.github.olszewskif.fridge.identity.domain.exception;

import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.identity.domain.UserId;
import io.github.olszewskif.fridge.shared.domain.DomainException;

public class UserNotFoundException extends DomainException {
    public UserNotFoundException(UserId id) {
        super("User with id " + id.getValue() + " not found!");
    }

    public UserNotFoundException(Email email) {
        super("User with email " + email + " not found!");
    }
}
