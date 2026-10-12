package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.inventory.domain.exception.FridgeItemNotFoundException;
import io.github.olszewskif.fridge.shared.domain.AggregateRoot;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public final class Fridge extends AggregateRoot<FridgeId> {

    private String name;
    private final Instant createdAt;
    private final UserId createdBy;
    private final List<FridgeItem> items;

    private Fridge(FridgeId id, String name, UserId createdBy) {
        this(id, name, Instant.now(), createdBy, new ArrayList<>());
    }

    private Fridge(FridgeId id, String name, Instant createdAt, UserId createdBy,
                   List<FridgeItem> fridgeItems) {
        super(id);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank!");
        }
        this.name = name;
        this.createdAt = Objects.requireNonNull(createdAt, "Creation time cannot be null!");
        this.createdBy = Objects.requireNonNull(createdBy, "User ID cannot be null!");
        this.items = fridgeItems == null ? new ArrayList<>() : new ArrayList<>(fridgeItems);
    }

    public static Fridge create(String name, UserId createdBy) {
        return new Fridge(FridgeId.generate(), name, createdBy);
    }

    public static Fridge restore(FridgeId id, String name, Instant createdAt, UserId createdBy,
                                 List<FridgeItem> fridgeItems) {
        return new Fridge(id, name, createdAt, createdBy, fridgeItems);
    }

    @Nonnull public String getName() {
        return name;
    }

    @Nonnull public Instant getCreatedAt() {
        return createdAt;
    }

    @Nonnull public UserId getCreatedBy() {
        return createdBy;
    }

    @Nonnull public List<FridgeItem> getItems() {
        return List.copyOf(this.items);
    }

    public void changeName(String newName) {
        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank!");
        }
        this.name = newName;
    }

    public FridgeItem addItem(ProductId productId, UserId addedBy, Quantity quantity,
                           @Nullable ExpiryDate expiryDate) {
        FridgeItem item = FridgeItem.create(productId, addedBy, quantity, expiryDate);
        this.items.add(item);
        return item;
    }

    public void removeItem(FridgeItemId id) {
        boolean removed = this.items.removeIf(i -> i.getId().equals(id));
        if (!removed) {
            throw new FridgeItemNotFoundException(id);
        }
    }

    public void removeItems(Set<FridgeItemId> ids) {
        this.items.removeIf(i -> ids.contains(i.getId()));
    }

    public void changeItemQuantity(FridgeItemId id, Quantity quantity) {
        FridgeItem item = getItem(id);
        item.changeQuantity(quantity);
    }

    public void changeItemExpiryDate(FridgeItemId id, @Nullable ExpiryDate expiryDate) {
        FridgeItem item = getItem(id);
        item.changeExpiryDate(expiryDate);
    }

    public FridgeItem getItem(FridgeItemId id) {
        return this.items.stream()
                .filter(i -> i.getId().equals(id))
                .findAny()
                .orElseThrow(() -> new FridgeItemNotFoundException(id));
    }

}
