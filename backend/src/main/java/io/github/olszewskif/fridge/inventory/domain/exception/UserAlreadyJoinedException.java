package io.github.olszewskif.fridge.inventory.domain.exception;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.FridgeItemId;
import io.github.olszewskif.fridge.inventory.domain.UserId;
import io.github.olszewskif.fridge.shared.domain.NotFoundException;

public class UserAlreadyJoinedException extends NotFoundException {
    public UserAlreadyJoinedException(FridgeId fridgeId, UserId userId) {
        super("User with id " + userId.getValue() + " has already joined fridge " + fridgeId.getValue() + ".");
    }
}
