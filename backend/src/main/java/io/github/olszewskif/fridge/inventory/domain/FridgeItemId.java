package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.shared.domain.DomainId;

import java.util.UUID;

public final class FridgeItemId extends DomainId {
    private FridgeItemId(UUID value) {
        super(value);
    }

    public static FridgeItemId generate() {
        return new FridgeItemId(UUID.randomUUID());
    }

    public static FridgeItemId of(UUID uuid) {
        return new FridgeItemId(uuid);
    }
}
