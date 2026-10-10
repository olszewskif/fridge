package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.*;

public record UpdateFridgeItemCommand(
        FridgeId fridgeId,
        FridgeItemId itemId,
        Amount amount,
        ExpiryDate expiryDate
) {
}
