package io.github.olszewskif.fridge.catalog.domain;

import io.github.olszewskif.fridge.shared.domain.DomainId;

import java.util.UUID;

public final class ProductId extends DomainId {

    private ProductId(UUID value) {
        super(value);
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID());
    }

    public static ProductId of(UUID uuid) {
        return new ProductId(uuid);
    }

}
