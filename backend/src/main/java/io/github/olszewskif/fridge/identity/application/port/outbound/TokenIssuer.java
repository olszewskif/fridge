package io.github.olszewskif.fridge.identity.application.port.outbound;

import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.identity.domain.UserId;
import io.github.olszewskif.fridge.identity.domain.UserRole;

public interface TokenIssuer {
    String generateToken(UserId userId, Email email, UserRole role);
}
