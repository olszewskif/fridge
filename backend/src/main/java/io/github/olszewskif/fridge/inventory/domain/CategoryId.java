package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.shared.domain.DomainId;

import java.util.UUID;

public final class CategoryId extends DomainId {
    private CategoryId(UUID value) {
        super(value);
    }

    public static CategoryId generate() {
        return new CategoryId(UUID.randomUUID());
    }

    public static CategoryId of(UUID uuid) {
        return new CategoryId(uuid);
    }
}
