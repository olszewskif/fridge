package io.github.olszewskif.fridge.identity.application.usecase;

import io.github.olszewskif.fridge.identity.application.dto.RegisterUserCommand;
import io.github.olszewskif.fridge.identity.application.dto.RegisterUserResult;

public interface AuthUseCase {
    RegisterUserResult register(RegisterUserCommand command);
}
