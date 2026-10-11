package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Min;

import java.util.Set;
import java.util.UUID;

public record FridgeItemsFilterRequest(
        @Nullable Boolean isExpired,
        @Nullable @Min(1) Integer expiresInDays,
        @Nullable UUID productId,
        @Nullable Set<UUID> categoryIds,
        @Nullable UUID addedBy,
        @Nullable String search
) {
}
