package io.github.olszewskif.fridge.inventory.adapter.inbound.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record AmountDto(@Min(1) int quantity, @NotBlank String unit) {
}
