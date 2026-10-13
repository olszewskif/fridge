package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.UserId;

public record JoinFridgeCommand(FridgeId fridgeId, UserId userId) {
}
