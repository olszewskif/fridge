package io.github.olszewskif.fridge.identity.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record RegisterUserRequest(
        @NotBlank String displayName,
        @NotBlank String email,
        @NotBlank String password
) {
}
