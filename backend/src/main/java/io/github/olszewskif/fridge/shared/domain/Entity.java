package io.github.olszewskif.fridge.shared.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode(of = "id")
public abstract class Entity<ID extends DomainId> {

    @Getter
    private final ID id;

    protected Entity(ID id) {
        this.id = id;
    }
}
