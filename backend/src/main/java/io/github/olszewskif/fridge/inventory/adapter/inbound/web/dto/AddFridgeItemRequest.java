package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.UUID;

public record AddFridgeItemRequest(
        @NotNull UUID productId,
        @Min(1) int quantity,
        @Nullable LocalDate expiryDate
) {}
