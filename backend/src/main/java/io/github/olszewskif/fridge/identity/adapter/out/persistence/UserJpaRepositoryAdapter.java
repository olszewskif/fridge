package io.github.olszewskif.fridge.identity.adapter.out.persistence;

import io.github.olszewskif.fridge.identity.adapter.out.persistence.mapper.UserJpaMapper;
import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.identity.domain.User;
import io.github.olszewskif.fridge.identity.domain.UserId;
import io.github.olszewskif.fridge.identity.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaRepositoryAdapter implements UserRepository {

    private final UserJpaRepository repository;
    private final UserJpaMapper mapper;

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpaEntity(user);
        repository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return repository.findById(id.getValue()).map(mapper::toDomain);
    }

}
