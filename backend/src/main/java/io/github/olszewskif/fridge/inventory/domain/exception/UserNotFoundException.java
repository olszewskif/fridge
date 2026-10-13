package io.github.olszewskif.fridge.inventory.domain.exception;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.UserId;
import io.github.olszewskif.fridge.shared.domain.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(FridgeId fridgeId, UserId userId) {
        super("User " + userId + " not found in fridge " + fridgeId + ".");
    }
}
