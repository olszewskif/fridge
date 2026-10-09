package io.github.olszewskif.fridge.shared.domain;

import java.time.Instant;
import java.util.UUID;

public interface DomainEvent {
    UUID id();
    Instant timestamp();
}
