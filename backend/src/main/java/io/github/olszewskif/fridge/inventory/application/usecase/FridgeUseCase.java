package io.github.olszewskif.fridge.inventory.application.usecase;

import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.inventory.domain.FridgeFilterQuery;
import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;

public interface FridgeUseCase {
    FridgeResult getFridge(FridgeId id);
    PageResult<FridgeResult> getFridges(FridgeFilterQuery filters, PageQuery pageQuery);
    FridgeResult createFridge(CreateFridgeCommand command);
    FridgeResult updateFridge(FridgeId id, UpdateFridgeCommand command);
    void deleteFridge(FridgeId id);
    void joinFridge(JoinFridgeCommand command);
    void leaveFridge(LeaveFridgeCommand command);
}
