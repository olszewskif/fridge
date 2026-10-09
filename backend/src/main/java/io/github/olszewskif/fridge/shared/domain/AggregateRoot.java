package io.github.olszewskif.fridge.shared.domain;

import java.util.ArrayList;
import java.util.List;

public abstract class AggregateRoot<ID extends DomainId> extends Entity<ID> {

    private final List<DomainEvent> events = new ArrayList<>();

    protected AggregateRoot(ID id) {
        super(id);
    }

    public void addEvent(DomainEvent event) {
        events.add(event);
    }

    public List<DomainEvent> getEvents() {
        return List.copyOf(events);
    }

    public void clearEvents() {
        events.clear();
    }

}
