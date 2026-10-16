package io.github.olszewskif.fridge.identity.application.usecase;

import io.github.olszewskif.fridge.identity.application.dto.*;

public interface AuthUseCase {
    RegisterUserResult register(RegisterUserCommand command);
    LoginUserResult login(LoginUserCommand command);
    RefreshTokenResult refresh(RefreshTokenCommand command);
    void logout(LogoutUserCommand command);
}
