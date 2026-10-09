package io.github.olszewskif.fridge.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.UUID;

@EqualsAndHashCode
public abstract class DomainId {

    @Getter
    private final UUID value;

    protected DomainId(UUID value) {
        this.value = value;
    }
}
