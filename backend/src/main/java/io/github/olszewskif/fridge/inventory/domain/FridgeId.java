package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.shared.domain.DomainId;

import java.util.UUID;

public final class FridgeId extends DomainId {
    private FridgeId(UUID value) {
        super(value);
    }

    public static FridgeId generate() {
        return new FridgeId(UUID.randomUUID());
    }

    public static FridgeId of(UUID uuid) {
        return new FridgeId(uuid);
    }
}
