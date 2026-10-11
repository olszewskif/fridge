package io.github.olszewskif.fridge.inventory.application.dto;

import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.ProductId;
import io.github.olszewskif.fridge.inventory.domain.UserId;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ResolvedFridgeItemsFilterQuery(
        @NotNull FridgeId fridgeId,
        @Nullable Boolean isExpired,
        @Nullable Integer expiresInDays,
        @Nullable UserId addedBy,
        @Nullable ProductId productId,
        @Nullable Set<ProductId> resolvedProductIds
) {
}
