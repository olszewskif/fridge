package io.github.olszewskif.fridge.identity.application.dto;

import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.identity.domain.UserRole;

public record SecurityUserDto(
        String displayName,
        Email email,
        String password,
        UserRole role
) {
}
