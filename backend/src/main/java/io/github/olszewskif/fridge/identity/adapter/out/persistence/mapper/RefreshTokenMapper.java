package io.github.olszewskif.fridge.identity.adapter.out.persistence.mapper;

import io.github.olszewskif.fridge.identity.adapter.out.persistence.RefreshTokenJpaEntity;
import io.github.olszewskif.fridge.identity.domain.RefreshToken;
import io.github.olszewskif.fridge.identity.domain.RefreshTokenId;
import io.github.olszewskif.fridge.identity.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class RefreshTokenMapper {

    public RefreshTokenJpaEntity toJpaEntity(RefreshToken refreshToken) {
        RefreshTokenJpaEntity entity = new RefreshTokenJpaEntity();
        entity.setId(refreshToken.getId().getValue());
        entity.setUserId(refreshToken.getUserId().getValue());
        entity.setToken(refreshToken.getToken());
        entity.setExpiryDate(refreshToken.getExpiryDate());
        entity.setRevoked(refreshToken.isRevoked());
        return entity;
    }

    public RefreshToken toDomain(RefreshTokenJpaEntity entity) {
        return RefreshToken.restore(
                RefreshTokenId.of(entity.getId()),
                UserId.of(entity.getUserId()),
                entity.getToken(),
                entity.getExpiryDate(),
                entity.isRevoked()
        );
    }
}
