package io.github.olszewskif.fridge.inventory.application.mapper;

import io.github.olszewskif.fridge.inventory.application.dto.FridgeResult;
import io.github.olszewskif.fridge.inventory.domain.Fridge;
import org.springframework.stereotype.Component;

@Component
public final class FridgeAppMapper {

    public FridgeResult toResult(Fridge fridge) {
        return new FridgeResult(
                fridge.getId(),
                fridge.getName(),
                fridge.getCreatedAt(),
                fridge.getCreatedBy()
        );
    }
}
