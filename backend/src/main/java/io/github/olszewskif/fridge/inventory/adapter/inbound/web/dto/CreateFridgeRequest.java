package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateFridgeRequest(
        @NotBlank String name
) {
}
