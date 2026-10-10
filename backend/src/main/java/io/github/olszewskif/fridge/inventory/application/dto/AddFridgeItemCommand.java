package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.Amount;
import io.github.olszewskif.fridge.inventory.domain.ExpiryDate;
import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.ProductId;

public record AddFridgeItemCommand(
        FridgeId fridgeId,
        ProductId productId,
        Amount amount,
        ExpiryDate expiryDate
) {
}
