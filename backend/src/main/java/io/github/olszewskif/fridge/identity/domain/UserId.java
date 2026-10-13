package io.github.olszewskif.fridge.identity.domain;

import io.github.olszewskif.fridge.shared.domain.DomainId;

import java.util.UUID;

public class UserId extends DomainId {
    private UserId(UUID value) {
        super(value);
    }

    public static UserId generate() {
        return new UserId(UUID.randomUUID());
    }

    public static UserId of(UUID uuid) {
        return new UserId(uuid);
    }
}
