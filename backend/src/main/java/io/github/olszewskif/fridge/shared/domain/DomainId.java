package io.github.olszewskif.fridge.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;
import java.util.UUID;

@ToString
@EqualsAndHashCode
public abstract class DomainId {

    @Getter
    private final UUID value;

    protected DomainId(UUID value) {
        this.value = Objects.requireNonNull(value, "ID cannot be null!");
    }
}
