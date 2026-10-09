package io.github.olszewskif.fridge.catalog.domain;

import io.github.olszewskif.fridge.shared.domain.AggregateRoot;
import lombok.Getter;

@Getter
public final class Category extends AggregateRoot<CategoryId> {

    private String name;
    private CategoryId parentId;

    private Category(CategoryId id, String name, CategoryId parentId) {
        super(id);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank!");
        }
        this.name = name;
        this.parentId = parentId;
    }

    public static Category create(String name, CategoryId parentId) {
        return new Category(CategoryId.generate(), name, parentId);
    }

    public static Category restore(CategoryId id, String name, CategoryId parentId) {
        return new Category(id, name, parentId);
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
}
