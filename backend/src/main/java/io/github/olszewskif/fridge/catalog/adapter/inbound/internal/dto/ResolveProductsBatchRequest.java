package io.github.olszewskif.fridge.catalog.adapter.inbound.internal.dto;

import java.util.Set;
import java.util.UUID;

public record ResolveProductsBatchRequest(Set<UUID> productIds) {
}
