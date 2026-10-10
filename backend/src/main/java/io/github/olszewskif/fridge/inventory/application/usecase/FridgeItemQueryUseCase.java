package io.github.olszewskif.fridge.inventory.application.usecase;

import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.shared.domain.PageResult;

public interface FridgeItemQueryUseCase {
    FridgeItemResult getItem(GetFridgeItemQuery query);
    PageResult<FridgeItemResult> getItems(GetFridgeItemsQuery query);
}
