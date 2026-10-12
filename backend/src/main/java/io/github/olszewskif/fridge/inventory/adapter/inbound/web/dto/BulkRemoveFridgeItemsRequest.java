package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import jakarta.validation.constraints.NotEmpty;

import java.util.Set;
import java.util.UUID;

public record BulkRemoveFridgeItemsRequest(@NotEmpty Set<UUID> itemIds) {
}
