package io.github.olszewskif.fridge.identity.application.dto;

import jakarta.annotation.Nonnull;

public record LogoutUserCommand(
        @Nonnull String refreshToken
) {
}
