package io.github.olszewskif.fridge.identity.domain;

import io.github.olszewskif.fridge.shared.domain.DomainId;

import java.util.UUID;

public class RefreshTokenId extends DomainId {
    private RefreshTokenId(UUID value) {
        super(value);
    }

    public static RefreshTokenId generate() {
        return new RefreshTokenId(UUID.randomUUID());
    }

    public static RefreshTokenId of(UUID uuid) {
        return new RefreshTokenId(uuid);
    }
}
