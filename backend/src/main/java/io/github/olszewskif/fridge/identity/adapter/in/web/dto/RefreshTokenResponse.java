package io.github.olszewskif.fridge.identity.adapter.in.web.dto;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {
}
