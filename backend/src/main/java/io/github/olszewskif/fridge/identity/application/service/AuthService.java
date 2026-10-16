package io.github.olszewskif.fridge.identity.application.service;

import io.github.olszewskif.fridge.identity.application.dto.*;
import io.github.olszewskif.fridge.identity.application.exception.InvalidCredentialsException;
import io.github.olszewskif.fridge.identity.application.port.outbound.LoadSecurityUser;
import io.github.olszewskif.fridge.identity.application.port.outbound.RefreshTokenIssuer;
import io.github.olszewskif.fridge.identity.application.port.outbound.SecurityPolicy;
import io.github.olszewskif.fridge.identity.application.port.outbound.TokenIssuer;
import io.github.olszewskif.fridge.identity.application.usecase.AuthUseCase;
import io.github.olszewskif.fridge.identity.domain.RefreshToken;
import io.github.olszewskif.fridge.identity.domain.User;
import io.github.olszewskif.fridge.identity.domain.UserId;
import io.github.olszewskif.fridge.identity.domain.exception.CompromisedTokenException;
import io.github.olszewskif.fridge.identity.domain.exception.InvalidRefreshTokenException;
import io.github.olszewskif.fridge.identity.domain.exception.UserNotFoundException;
import io.github.olszewskif.fridge.identity.domain.port.out.RefreshTokenRepository;
import io.github.olszewskif.fridge.identity.domain.port.out.UniqueEmailChecker;
import io.github.olszewskif.fridge.identity.domain.port.out.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService implements AuthUseCase {

    private final UserRepository userRepository;
    private final UniqueEmailChecker emailChecker;
    private final PasswordEncoder passwordEncoder;
    private final TokenIssuer tokenIssuer;
    private final LoadSecurityUser loadSecurityUser;
    private final RefreshTokenIssuer refreshTokenIssuer;
    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityPolicy securityPolicy;

    @Override
    public RegisterUserResult register(RegisterUserCommand command) {
        User user = User.create(
                command.displayName(),
                command.email(),
                passwordEncoder.encode(command.password()),
                emailChecker
        );
        userRepository.save(user);
        String token = tokenIssuer.generateToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = generateHashSaveAndGetRefreshToken(user.getId());
        return new RegisterUserResult(user.getId(), token, refreshToken);
    }

    @Override
    public LoginUserResult login(LoginUserCommand command) {
        SecurityUserDto user = loadSecurityUser.findUserByEmail(command.email())
                .filter(u -> passwordEncoder.matches(command.password(), u.password()))
                .orElseThrow(InvalidCredentialsException::new);
        String token = tokenIssuer.generateToken(user.userId(), user.email(), user.role());
        String refreshToken = generateHashSaveAndGetRefreshToken(user.userId());
        return new LoginUserResult(user.userId(), token, refreshToken);
    }

    @Override
    public RefreshTokenResult refresh(RefreshTokenCommand command) {
        String hashedToken = refreshTokenIssuer.hashRefreshToken(command.rawToken());
        RefreshToken storedToken = refreshTokenRepository.findByToken(hashedToken)
                .orElseThrow(InvalidRefreshTokenException::new);
        UserId tokenUserId = storedToken.getUserId();

        if (storedToken.isRevoked()) {
            refreshTokenRepository.revokeAllByUserId(tokenUserId);
            throw new CompromisedTokenException("Token reuse detected! All sessions revoked.");
        }

        if (!storedToken.isValid()) {
            throw new InvalidRefreshTokenException("Refresh accessToken expired or has been revoked!");
        }

        storedToken.revokeToken();
        refreshTokenRepository.save(storedToken);

        User user = userRepository.findById(tokenUserId)
                .orElseThrow(() -> new UserNotFoundException(tokenUserId));
        String newAccessToken = tokenIssuer.generateToken(tokenUserId, user.getEmail(), user.getRole());
        String newRefreshToken = generateHashSaveAndGetRefreshToken(tokenUserId);

        return new RefreshTokenResult(newAccessToken, newRefreshToken);
    }

    @Override
    public void logout(LogoutUserCommand command) {
        String hash = refreshTokenIssuer.hashRefreshToken(command.refreshToken());
        refreshTokenRepository.revokeByToken(hash);
    }

    private String generateHashSaveAndGetRefreshToken(UserId userId) {
        String token = refreshTokenIssuer.generateRefreshToken();
        String hashed = refreshTokenIssuer.hashRefreshToken(token);
        Instant expiryDate = Instant.now().plus(securityPolicy.getRefreshTokenDuration());

        RefreshToken refreshToken = RefreshToken.create(userId, hashed, expiryDate);
        refreshTokenRepository.save(refreshToken);
        return token;
    }
}
