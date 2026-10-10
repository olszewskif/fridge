package io.github.olszewskif.fridge.inventory.application.mapper;

import io.github.olszewskif.fridge.inventory.application.dto.FridgeItemResult;
import io.github.olszewskif.fridge.inventory.application.dto.FridgeResult;
import io.github.olszewskif.fridge.inventory.domain.Fridge;
import io.github.olszewskif.fridge.inventory.domain.FridgeItem;
import org.springframework.stereotype.Component;

@Component
public final class FridgeItemAppMapper {

    public FridgeItemResult toResult(FridgeItem fridgeItem) {
        return new FridgeItemResult(
                fridgeItem.getId(),
                fridgeItem.getProductId(),
                fridgeItem.getAddedBy(),
                fridgeItem.getAddedAt(),
                fridgeItem.getAmount(),
                fridgeItem.getExpiryDate()
        );
    }
}
