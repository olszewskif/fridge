package io.github.olszewskif.fridge.identity.domain;

import io.github.olszewskif.fridge.identity.domain.exception.RefreshTokenAlreadyRevokedException;
import io.github.olszewskif.fridge.shared.domain.AggregateRoot;
import io.github.olszewskif.fridge.shared.domain.DomainUtil;
import jakarta.annotation.Nonnull;

import java.time.Instant;

public class RefreshToken extends AggregateRoot<RefreshTokenId> {

    private final UserId userId;
    private final String token;
    private final Instant expiryDate;
    private boolean revoked;

    private RefreshToken(RefreshTokenId id, UserId userId, String token, Instant expiryDate) {
        this(id, userId, token, expiryDate, false);
    }

    private RefreshToken(RefreshTokenId id, UserId userId, String token, Instant expiryDate,
                         boolean revoked) {
        super(id);
        this.userId = DomainUtil.notNull(userId, "User ID cannot be null!");
        this.token = DomainUtil.notBlank(token, "Token cannot be null or blank!");
        this.expiryDate = DomainUtil.notNull(expiryDate, "Expiry date cannot be null!");
        this.revoked = revoked;
    }

    public static RefreshToken create(UserId userId, String token, Instant expiryDate) {
        return new RefreshToken(RefreshTokenId.generate(), userId, token, expiryDate);
    }

    public static RefreshToken restore(RefreshTokenId id, UserId userId, String token,
                                       Instant expiryDate, boolean revoked) {
        return new RefreshToken(id, userId, token, expiryDate, revoked);
    }

    @Nonnull public UserId getUserId() {
        return userId;
    }

    @Nonnull public String getToken() {
        return token;
    }

    @Nonnull public Instant getExpiryDate() {
        return expiryDate;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void revokeToken() {
        if (revoked) {
            throw new RefreshTokenAlreadyRevokedException(this.id);
        }
        revoked = true;
    }

    public boolean isValid() {
        return !revoked && expiryDate.isAfter(Instant.now());
    }
}
