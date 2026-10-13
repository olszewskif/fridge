package io.github.olszewskif.fridge.identity.adapter.out.persistence;

import io.github.olszewskif.fridge.identity.domain.Email;
import io.github.olszewskif.fridge.identity.domain.port.out.UniqueEmailChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniqueEmailCheckerAdapter implements UniqueEmailChecker {

    private final UserJpaRepository userJpaRepository;

    @Override
    public boolean isUnique(Email email) {
        return !userJpaRepository.existsByEmail(email.value());
    }
}
