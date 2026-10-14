package io.github.olszewskif.fridge.identity.adapter.out.persistence.mapper;

import io.github.olszewskif.fridge.identity.adapter.out.persistence.UserJpaEntity;
import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.identity.domain.User;
import io.github.olszewskif.fridge.identity.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class UserJpaMapper {

    public UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId().getValue());
        entity.setDisplayName(user.getDisplayName());
        entity.setEmail(user.getEmail().value());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        return entity;
    }

    public User toDomain(UserJpaEntity entity) {
        return User.restore(
                UserId.of(entity.getId()),
                entity.getDisplayName(),
                new Email(entity.getEmail()),
                entity.getPassword(),
                entity.getRole()
        );
    }
}
