package io.github.olszewskif.fridge.catalog.domain;

import io.github.olszewskif.fridge.shared.domain.AggregateRoot;
import lombok.Getter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Getter
public final class Category extends AggregateRoot<CategoryId> {

    private String name;
    private CategoryId parentId;
    private final Set<Alias> aliases;

    private Category(CategoryId id, String name, CategoryId parentId, Set<Alias> aliases) {
        super(id);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank!");
        }
        this.name = name;
        this.parentId = parentId;
        this.aliases = aliases == null ? new HashSet<>() : new HashSet<>(aliases);
    }

    public static Category create(String name, CategoryId parentId) {
        return new Category(CategoryId.generate(), name, parentId, Collections.emptySet());
    }

    public static Category create(String name, CategoryId parentId, Set<Alias> aliases) {
        return new Category(CategoryId.generate(), name, parentId, aliases);
    }

    public static Category restore(CategoryId id, String name, CategoryId parentId) {
        return new Category(id, name, parentId, Collections.emptySet());
    }

    public static Category restore(CategoryId id, String name, CategoryId parentId, Set<Alias> aliases) {
        return new Category(id, name, parentId, aliases);
    }

    public Set<Alias> getAliases() {
        return Collections.unmodifiableSet(aliases);
    }

    public void updateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank!");
        }
        this.name = name;
    }

    public void updateParent(CategoryId parentId) {
        this.parentId = parentId;
    }

    public void updateAliases(Set<Alias> newAliases) {
        this.aliases.clear();
        if (newAliases != null) {
            this.aliases.addAll(newAliases);
        }
    }

    public void addAlias(Alias alias) {
        if (alias != null) {
            this.aliases.add(alias);
        }
    }
}
