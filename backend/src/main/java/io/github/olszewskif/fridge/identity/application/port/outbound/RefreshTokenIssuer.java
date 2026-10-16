package io.github.olszewskif.fridge.identity.application.port.outbound;

public interface RefreshTokenIssuer {
    String generateRefreshToken();
    String hashRefreshToken(String rawToken);
}
