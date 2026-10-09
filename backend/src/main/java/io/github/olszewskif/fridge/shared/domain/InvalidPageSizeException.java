package io.github.olszewskif.fridge.shared.domain;

public class InvalidPageSizeException extends DomainException {
    public InvalidPageSizeException() {
        super("Page size must be more than 0!");
    }
}
