package io.github.olszewskif.fridge.identity.domain.exception;

import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.shared.domain.DomainException;

public class EmailAlreadyExistsException extends DomainException {
    public EmailAlreadyExistsException(Email email) {
        super("User with email " + email.value() + " already exists!");
    }
}
