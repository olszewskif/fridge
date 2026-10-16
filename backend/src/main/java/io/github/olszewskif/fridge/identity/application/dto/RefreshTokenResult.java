package io.github.olszewskif.fridge.identity.application.dto;

public record RefreshTokenResult(
        String accessToken,
        String refreshToken
) {
}
