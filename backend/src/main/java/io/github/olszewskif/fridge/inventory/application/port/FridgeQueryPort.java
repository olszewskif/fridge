package io.github.olszewskif.fridge.inventory.application.port;

import io.github.olszewskif.fridge.inventory.application.dto.FridgeItemResult;
import io.github.olszewskif.fridge.inventory.application.dto.GetFridgeItemQuery;
import io.github.olszewskif.fridge.inventory.application.dto.GetFridgeItemsQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;

import java.util.Optional;

public interface FridgeQueryPort {
    PageResult<FridgeItemResult> findAllByFridgeId(GetFridgeItemsQuery query);
    Optional<FridgeItemResult> findById(GetFridgeItemQuery query);
}
