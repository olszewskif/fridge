package io.github.olszewskif.fridge.inventory.domain.exception;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.UserId;
import io.github.olszewskif.fridge.shared.domain.NotFoundException;

public class UserLimitReachedException extends NotFoundException {
    public UserLimitReachedException(FridgeId fridgeId, int number) {
        super("Fridge " + fridgeId + " has already reached a maximum limit of users (" + number + ").");
    }
}
