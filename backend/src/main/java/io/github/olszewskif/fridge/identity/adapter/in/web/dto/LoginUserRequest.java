package io.github.olszewskif.fridge.identity.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginUserRequest(
        @NotBlank String email,
        @NotBlank String password
) {
}
