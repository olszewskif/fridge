package io.github.olszewskif.fridge.identity.adapter.in.web.dto;

import jakarta.validation.constraints.NotNull;

public record LogoutUserRequest(
        @NotNull String refreshToken
) {
}
