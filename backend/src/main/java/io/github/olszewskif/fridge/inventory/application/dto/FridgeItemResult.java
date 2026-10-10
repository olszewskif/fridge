package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.*;

import java.time.Instant;

public record FridgeItemResult(
        FridgeItemId id,
        ProductId productId,
        UserId addedBy,
        Instant addedAt,
        Amount amount,
        ExpiryDate expiryDate
) {
}
