package io.github.olszewskif.fridge.identity.application.usecase;

import io.github.olszewskif.fridge.identity.application.dto.LoginUserCommand;
import io.github.olszewskif.fridge.identity.application.dto.LoginUserResult;
import io.github.olszewskif.fridge.identity.application.dto.RegisterUserCommand;
import io.github.olszewskif.fridge.identity.application.dto.RegisterUserResult;

public interface AuthUseCase {
    RegisterUserResult register(RegisterUserCommand command);
    LoginUserResult login(LoginUserCommand command);
}
