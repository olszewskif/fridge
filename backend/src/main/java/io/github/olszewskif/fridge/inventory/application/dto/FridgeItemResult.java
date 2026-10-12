package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.*;
import jakarta.annotation.Nullable;

import java.time.Instant;

public record FridgeItemResult(
        FridgeItemId id,
        ProductId productId,
        UserId addedBy,
        Instant addedAt,
        Quantity quantity,
        @Nullable ExpiryDate expiryDate,
        @Nullable ProductSummaryResult product
) {
    public FridgeItemResult(
            FridgeItemId id,
            ProductId productId,
            UserId addedBy,
            Instant addedAt,
            Quantity quantity,
            @Nullable ExpiryDate expiryDate
    ) {
        this(id, productId, addedBy, addedAt, quantity, expiryDate, null);
    }

    public FridgeItemResult withProduct(@Nullable ProductSummaryResult product) {
        return new FridgeItemResult(id, productId, addedBy, addedAt, quantity, expiryDate, product);
    }
}
