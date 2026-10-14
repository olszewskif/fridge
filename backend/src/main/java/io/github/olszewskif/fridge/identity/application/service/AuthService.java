package io.github.olszewskif.fridge.identity.application.service;

import io.github.olszewskif.fridge.identity.application.dto.RegisterUserCommand;
import io.github.olszewskif.fridge.identity.application.dto.RegisterUserResult;
import io.github.olszewskif.fridge.identity.application.usecase.AuthUseCase;
import io.github.olszewskif.fridge.identity.domain.User;
import io.github.olszewskif.fridge.identity.domain.port.out.UniqueEmailChecker;
import io.github.olszewskif.fridge.identity.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final UserRepository repository;
    private final UniqueEmailChecker emailChecker;
    private final PasswordEncoder passwordEncoder;

    @Override
    public RegisterUserResult register(RegisterUserCommand command) {
        User user = User.create(
                command.displayName(),
                command.email(),
                passwordEncoder.encode(command.password()),
                emailChecker
        );
        repository.save(user);
        return new RegisterUserResult(user.getId(), "some-jwt");
    }
}
