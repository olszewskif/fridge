package io.github.olszewskif.fridge.identity.adapter.in.web.dto;

import java.util.UUID;

public record RegisterUserResponse(
        UUID userId,
        String jwt
) {
}
