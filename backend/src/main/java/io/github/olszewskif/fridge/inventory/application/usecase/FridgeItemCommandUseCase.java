package io.github.olszewskif.fridge.inventory.application.usecase;

import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.shared.domain.PageResult;

public interface FridgeItemCommandUseCase {
    FridgeItemResult addItem(AddFridgeItemCommand command);
    FridgeItemResult updateItem(UpdateFridgeItemCommand command);
    void removeItem(RemoveFridgeItemCommand command);
}
