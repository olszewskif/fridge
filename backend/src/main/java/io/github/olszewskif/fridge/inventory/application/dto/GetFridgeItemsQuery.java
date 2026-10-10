package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.FridgeItemId;
import io.github.olszewskif.fridge.shared.domain.PageQuery;

public record GetFridgeItemsQuery(FridgeId fridgeId, PageQuery pageQuery) {
}
