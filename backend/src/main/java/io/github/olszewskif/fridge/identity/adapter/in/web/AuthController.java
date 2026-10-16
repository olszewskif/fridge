package io.github.olszewskif.fridge.identity.adapter.in.web;

import io.github.olszewskif.fridge.identity.adapter.in.web.dto.*;
import io.github.olszewskif.fridge.identity.adapter.in.web.mapper.AuthWebMapper;
import io.github.olszewskif.fridge.identity.application.dto.LogoutUserCommand;
import io.github.olszewskif.fridge.identity.application.usecase.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthUseCase useCase;
    private final AuthWebMapper mapper;

    @PostMapping("/signup")
    public RegisterUserResponse registerUser(@RequestBody @Valid RegisterUserRequest request) {
        var command = mapper.toCommand(request);
        return mapper.toResponse(useCase.register(command));
    }

    @PostMapping("/login")
    public LoginUserResponse loginUser(@RequestBody @Valid LoginUserRequest request) {
        var command = mapper.toCommand(request);
        return mapper.toResponse(useCase.login(command));
    }

    @PostMapping("/refresh")
    public RefreshTokenResponse refreshToken(@RequestBody @Valid RefreshTokenRequest request) {
        var command = mapper.toCommand(request);
        return mapper.toResponse(useCase.refresh(command));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/logout")
    public void logout(@RequestBody @Valid LogoutUserRequest request) {
        useCase.logout(new LogoutUserCommand(request.refreshToken()));
    }
}
