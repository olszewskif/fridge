package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import java.time.Instant;
import java.util.UUID;

public record FridgeResponse(
        UUID id,
        String name,
        Instant createdAt,
        UUID createdBy
) {
}
