package io.github.olszewskif.fridge.identity.adapter.out.security;

import io.github.olszewskif.fridge.identity.application.port.outbound.SecurityPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class SpringSecurityPolicyAdapter implements SecurityPolicy {

    @Value("${app.security.jwt.refresh-token-expiration-d:30d}")
    private Duration refreshTokenDuration;

    @Override
    public Duration getRefreshTokenDuration() {
        return refreshTokenDuration;
    }
}
