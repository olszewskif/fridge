package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import java.util.Set;
import java.util.UUID;

public record ProductFilterRequest(String search, Set<UUID> categoryIds) {
}
