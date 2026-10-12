package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name,
        UUID parentId,
        Set<String> aliases
) {
    public CategoryResponse(UUID id, String name, UUID parentId) {
        this(id, name, parentId, Collections.emptySet());
    }
}
