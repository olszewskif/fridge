package io.github.olszewskif.fridge.identity.application.dto;

import io.github.olszewskif.fridge.identity.domain.UserId;

public record RegisterUserResult(
        UserId userId,
        String accessToken,
        String refreshToken
) {
}
