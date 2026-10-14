package io.github.olszewskif.fridge.identity.adapter.in.web.mapper;

import io.github.olszewskif.fridge.identity.adapter.in.web.dto.RegisterUserRequest;
import io.github.olszewskif.fridge.identity.adapter.in.web.dto.RegisterUserResponse;
import io.github.olszewskif.fridge.identity.application.dto.RegisterUserCommand;
import io.github.olszewskif.fridge.identity.application.dto.RegisterUserResult;
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
                result.token()
        );
    }
}
