package io.github.olszewskif.fridge.identity.adapter.in.web;

import io.github.olszewskif.fridge.identity.adapter.in.web.dto.RegisterUserRequest;
import io.github.olszewskif.fridge.identity.adapter.in.web.dto.RegisterUserResponse;
import io.github.olszewskif.fridge.identity.adapter.in.web.mapper.AuthWebMapper;
import io.github.olszewskif.fridge.identity.application.usecase.AuthUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
