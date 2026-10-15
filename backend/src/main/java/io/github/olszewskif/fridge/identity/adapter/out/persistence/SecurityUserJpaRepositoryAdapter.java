package io.github.olszewskif.fridge.identity.adapter.out.persistence;

import io.github.olszewskif.fridge.identity.application.dto.SecurityUserDto;
import io.github.olszewskif.fridge.identity.application.port.outbound.LoadSecurityUser;
import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.identity.domain.UserId;
import io.github.olszewskif.fridge.identity.domain.UserRole;
import io.github.olszewskif.fridge.identity.domain.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityUserJpaRepositoryAdapter implements LoadSecurityUser {

    private final UserJpaRepository repository;

    @Override
    public Optional<SecurityUserDto> findUserByEmail(Email email) {
        Optional<UserJpaEntity> entity = repository.findByEmail(email.value());
        return entity.map(e -> new SecurityUserDto(
                UserId.of(e.getId()),
                e.getDisplayName(),
                new Email(e.getEmail()),
                e.getPassword(),
                e.getRole()
        ));
    }
}
