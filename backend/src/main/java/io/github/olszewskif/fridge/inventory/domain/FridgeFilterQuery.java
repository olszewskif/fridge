package io.github.olszewskif.fridge.inventory.domain;

public record FridgeFilterQuery(
        String search,
        UserId ownerId
) {}
