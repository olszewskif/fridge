package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.FridgeItemId;

public record RemoveFridgeItemCommand(FridgeId fridgeId, FridgeItemId fridgeItemId) {
}
