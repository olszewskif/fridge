package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import java.util.UUID;

public record CategoryFilterRequest(
        String search,
        UUID parentId,
        Boolean isRoot
) {
}
