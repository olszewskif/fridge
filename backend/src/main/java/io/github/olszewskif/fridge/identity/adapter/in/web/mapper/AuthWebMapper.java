package io.github.olszewskif.fridge.identity.adapter.in.web.mapper;

import io.github.olszewskif.fridge.identity.adapter.in.web.dto.*;
import io.github.olszewskif.fridge.identity.application.dto.*;
import io.github.olszewskif.fridge.identity.domain.Email;
import org.springframework.stereotype.Component;

@Component
public class AuthWebMapper {

    public RegisterUserCommand toCommand(RegisterUserRequest request) {
        return new RegisterUserCommand(
                request.displayName(),
                new Email(request.email()),
                request.password()
        );
    }

    public RegisterUserResponse toResponse(RegisterUserResult result) {
        return new RegisterUserResponse(
                result.userId().getValue(),
                result.accessToken(),
                result.refreshToken()
        );
    }

    public LoginUserCommand toCommand(LoginUserRequest request) {
        return new LoginUserCommand(
                new Email(request.email()),
                request.password()
        );
    }

    public LoginUserResponse toResponse(LoginUserResult result) {
        return new LoginUserResponse(
                result.userId().getValue(),
                result.accessToken(),
                result.refreshToken()
        );
    }

    public RefreshTokenCommand toCommand(RefreshTokenRequest request) {
        return new RefreshTokenCommand(request.token());
    }

    public RefreshTokenResponse toResponse(RefreshTokenResult result) {
        return new RefreshTokenResponse(result.accessToken(), result.refreshToken());
    }
}
