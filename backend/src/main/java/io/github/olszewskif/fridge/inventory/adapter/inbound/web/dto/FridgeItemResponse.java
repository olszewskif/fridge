package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public record FridgeItemResponse(
        UUID id,
        UUID productId,
        UUID addedBy,
        Instant addedAt,
        int quantity,
        LocalDate expiryDate
) {
}
