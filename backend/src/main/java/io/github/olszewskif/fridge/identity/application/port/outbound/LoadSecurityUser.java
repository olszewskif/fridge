package io.github.olszewskif.fridge.identity.application.port.outbound;

import io.github.olszewskif.fridge.identity.application.dto.SecurityUserDto;
import io.github.olszewskif.fridge.identity.domain.Email;

import java.util.Optional;

public interface LoadSecurityUser {
    Optional<SecurityUserDto> findUserByEmail(Email email);
}
