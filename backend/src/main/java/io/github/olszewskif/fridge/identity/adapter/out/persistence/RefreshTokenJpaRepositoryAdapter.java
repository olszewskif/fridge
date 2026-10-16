package io.github.olszewskif.fridge.identity.adapter.out.persistence;

import io.github.olszewskif.fridge.identity.adapter.out.persistence.mapper.RefreshTokenMapper;
import io.github.olszewskif.fridge.identity.domain.RefreshToken;
import io.github.olszewskif.fridge.identity.domain.UserId;
import io.github.olszewskif.fridge.identity.domain.port.out.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RefreshTokenJpaRepositoryAdapter implements RefreshTokenRepository {

    private final RefreshTokenJpaRepository repository;
    private final RefreshTokenMapper mapper;

    @Override
    public RefreshToken save(RefreshToken refreshToken) {
        RefreshTokenJpaEntity entity = mapper.toJpaEntity(refreshToken);
        repository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return repository.findByToken(token).map(mapper::toDomain);
    }

    @Override
    public void revokeAllByUserId(UserId userId) {
        repository.revokeAllByUserId(userId.getValue());
    }

    @Override
    public void revokeByToken(String token) {
        repository.revokeByToken(token);
    }
}
