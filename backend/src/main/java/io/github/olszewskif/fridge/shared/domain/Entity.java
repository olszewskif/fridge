package io.github.olszewskif.fridge.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Objects;

@EqualsAndHashCode(of = "id")
public abstract class Entity<ID extends DomainId> {

    @Getter
    protected final ID id;

    protected Entity(ID id) {
        this.id = Objects.requireNonNull(id, "ID cannot be null!");
    }
}
