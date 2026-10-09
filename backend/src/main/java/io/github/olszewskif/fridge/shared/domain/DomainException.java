package io.github.olszewskif.fridge.shared.domain;

public abstract class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}
