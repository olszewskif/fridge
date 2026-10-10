package io.github.olszewskif.fridge.inventory.domain.exception;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.shared.domain.NotFoundException;

public class FridgeNotFoundException extends NotFoundException {
    public FridgeNotFoundException(FridgeId id) {
        super("Fridge with id " + id.getValue() + " not found");
    }
}
