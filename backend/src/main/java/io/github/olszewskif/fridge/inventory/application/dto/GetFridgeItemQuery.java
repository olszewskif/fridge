package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.FridgeItemId;

public record GetFridgeItemQuery(FridgeItemId fridgeItemId) {
}
