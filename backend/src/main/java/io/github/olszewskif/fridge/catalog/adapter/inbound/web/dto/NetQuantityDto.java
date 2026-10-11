package io.github.olszewskif.fridge.catalog.adapter.inbound.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NetQuantityDto(
        @Min(1) @NotNull double amount,
        @NotBlank String unit
) {
}
