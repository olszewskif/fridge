package io.github.olszewskif.fridge.shared.domain;

public abstract class NotFoundException extends DomainException {
    public NotFoundException(String message) {
        super(message);
    }
}
