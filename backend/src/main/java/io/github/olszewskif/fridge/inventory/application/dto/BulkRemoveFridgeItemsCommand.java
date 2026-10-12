package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.FridgeItemId;

import java.util.Set;

public record BulkRemoveFridgeItemsCommand(FridgeId fridgeId, Set<FridgeItemId> itemIds) {
}
