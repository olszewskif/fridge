package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.ExpiryDate;
import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.ProductId;
import io.github.olszewskif.fridge.inventory.domain.Quantity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

public record AddFridgeItemCommand(
        FridgeId fridgeId,
        ProductId productId,
        Quantity quantity,
        @Nullable ExpiryDate expiryDate
) {
}
