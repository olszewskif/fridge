package io.github.olszewskif.fridge.identity.application.dto;

import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.identity.domain.UserId;
import io.github.olszewskif.fridge.identity.domain.UserRole;

public record SecurityUserDto(
        UserId userId,
        String displayName,
        Email email,
        String password,
        UserRole role
) {
}
