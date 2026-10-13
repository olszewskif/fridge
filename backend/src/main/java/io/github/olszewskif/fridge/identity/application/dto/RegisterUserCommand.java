package io.github.olszewskif.fridge.identity.application.dto;

import io.github.olszewskif.fridge.identity.domain.Email;
import jakarta.annotation.Nonnull;

public record RegisterUserCommand(
        @Nonnull String displayName,
        @Nonnull Email email,
        @Nonnull String password
) {
}
