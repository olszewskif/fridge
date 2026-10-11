package io.github.olszewskif.fridge.inventory.domain;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record FridgeItemsFilterQuery(
        @NotNull FridgeId fridgeId,
        @Nullable Boolean isExpired,
        @Nullable Integer expiresInDays,
        @Nullable ProductId productId,
        @Nullable Set<CategoryId> categoryIds,
        @Nullable UserId addedBy,
        @Nullable String search
) {
}
