package io.github.olszewskif.fridge.identity.application.service;

import io.github.olszewskif.fridge.identity.adapter.in.security.JwtService;
import io.github.olszewskif.fridge.identity.application.dto.*;
import io.github.olszewskif.fridge.identity.application.exception.InvalidCredentialsException;
import io.github.olszewskif.fridge.identity.application.port.outbound.LoadSecurityUser;
import io.github.olszewskif.fridge.identity.application.port.outbound.TokenIssuer;
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
    private final TokenIssuer tokenIssuer;
    private final LoadSecurityUser loadSecurityUser;

    @Override
    public RegisterUserResult register(RegisterUserCommand command) {
        User user = User.create(
                command.displayName(),
                command.email(),
                passwordEncoder.encode(command.password()),
                emailChecker
        );
        repository.save(user);
        String token = tokenIssuer.generateToken(user.getId(), user.getEmail(), user.getRole());
        return new RegisterUserResult(user.getId(), token);
    }

    @Override
    public LoginUserResult login(LoginUserCommand command) {
        SecurityUserDto user = loadSecurityUser.findUserByEmail(command.email())
                .filter(u -> passwordEncoder.matches(command.password(), u.password()))
                .orElseThrow(InvalidCredentialsException::new);
        String token = tokenIssuer.generateToken(user.userId(), user.email(), user.role());
        return new LoginUserResult(user.userId(), token);
    }
}
