package io.github.olszewskif.fridge.identity.domain.port.out;

import io.github.olszewskif.fridge.identity.domain.RefreshToken;
import io.github.olszewskif.fridge.identity.domain.UserId;

import java.util.Optional;

public interface RefreshTokenRepository {
    RefreshToken save(RefreshToken refreshToken);
    Optional<RefreshToken> findByToken(String token);
    void revokeAllByUserId(UserId userId);
    void revokeByToken(String token);
}
