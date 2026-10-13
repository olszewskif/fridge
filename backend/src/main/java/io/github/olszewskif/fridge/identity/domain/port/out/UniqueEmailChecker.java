package io.github.olszewskif.fridge.identity.domain.port.out;

import io.github.olszewskif.fridge.identity.domain.Email;

@FunctionalInterface
public interface UniqueEmailChecker {
    boolean isUnique(Email email);
}
