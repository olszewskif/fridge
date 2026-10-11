package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.time.LocalDate;

public record UpdateFridgeItemRequest(
        @Nullable @Min(1) Integer quantity,
        @Nullable LocalDate expiryDate
) {
}
