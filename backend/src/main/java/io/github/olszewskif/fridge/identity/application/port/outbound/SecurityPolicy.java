package io.github.olszewskif.fridge.identity.application.port.outbound;

import java.time.Duration;

public interface SecurityPolicy {
    Duration getRefreshTokenDuration();
}
