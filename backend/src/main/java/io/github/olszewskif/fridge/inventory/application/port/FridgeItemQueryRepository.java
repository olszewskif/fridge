package io.github.olszewskif.fridge.inventory.application.port;

import io.github.olszewskif.fridge.inventory.application.dto.FridgeItemResult;
import io.github.olszewskif.fridge.inventory.application.dto.GetFridgeItemQuery;
import io.github.olszewskif.fridge.inventory.application.dto.ResolvedFridgeItemsFilterQuery;
import io.github.olszewskif.fridge.inventory.domain.FridgeItemsFilterQuery;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;

import java.util.Optional;

public interface FridgeItemQueryRepository {
    Optional<FridgeItemResult> findById(GetFridgeItemQuery query);
    PageResult<FridgeItemResult> findAll(ResolvedFridgeItemsFilterQuery filters, PageQuery pageQuery);
}
