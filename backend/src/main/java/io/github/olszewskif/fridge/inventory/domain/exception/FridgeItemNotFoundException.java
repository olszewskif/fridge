package io.github.olszewskif.fridge.inventory.domain.exception;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.FridgeItem;
import io.github.olszewskif.fridge.inventory.domain.FridgeItemId;
import io.github.olszewskif.fridge.shared.domain.NotFoundException;

public class FridgeItemNotFoundException extends NotFoundException {
    public FridgeItemNotFoundException(FridgeItemId id) {
        super("Fridge with id " + id.getValue() + " not found");
    }
}
