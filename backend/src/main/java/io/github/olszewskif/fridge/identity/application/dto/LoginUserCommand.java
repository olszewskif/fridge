package io.github.olszewskif.fridge.identity.application.dto;

import io.github.olszewskif.fridge.identity.domain.Email;

public record LoginUserCommand(
        Email email,
        String password
) {
}
