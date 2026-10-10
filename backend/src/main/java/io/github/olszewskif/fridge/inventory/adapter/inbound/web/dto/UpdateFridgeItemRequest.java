package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record UpdateFridgeItemRequest(
        @Valid AmountDto amount,
        LocalDate expiryDate
) {
}
