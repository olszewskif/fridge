package io.github.olszewskif.fridge.identity.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(
        @NotBlank String token
) {
}
