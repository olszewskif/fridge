package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        UUID parentId
) {
}
