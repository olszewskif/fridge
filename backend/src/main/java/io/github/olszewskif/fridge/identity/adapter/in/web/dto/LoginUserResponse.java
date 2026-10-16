package io.github.olszewskif.fridge.identity.adapter.in.web.dto;

import java.util.UUID;

public record LoginUserResponse(
        UUID userId,
        String accessToken,
        String refreshToken
) {
}
