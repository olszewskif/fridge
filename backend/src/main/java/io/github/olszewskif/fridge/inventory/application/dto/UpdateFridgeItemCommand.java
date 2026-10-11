package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.*;
import jakarta.annotation.Nullable;

public record UpdateFridgeItemCommand(
        FridgeId fridgeId,
        FridgeItemId itemId,
        Quantity quantity,
        @Nullable ExpiryDate expiryDate
) {
}
