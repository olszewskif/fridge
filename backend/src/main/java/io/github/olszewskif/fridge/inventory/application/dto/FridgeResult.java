package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.UserId;

import java.time.Instant;

public record FridgeResult(
        FridgeId id,
        String name,
        Instant createdAt,
        UserId createdBy
) {
}
