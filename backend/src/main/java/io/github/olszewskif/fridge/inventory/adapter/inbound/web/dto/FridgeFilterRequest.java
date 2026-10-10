package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import java.util.UUID;

public record FridgeFilterRequest(
        String search,
        UUID ownerId
) {}
