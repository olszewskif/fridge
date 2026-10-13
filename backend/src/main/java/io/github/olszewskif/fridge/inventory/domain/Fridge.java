package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.inventory.domain.exception.FridgeItemNotFoundException;
import io.github.olszewskif.fridge.inventory.domain.exception.UserAlreadyJoinedException;
import io.github.olszewskif.fridge.inventory.domain.exception.UserLimitReachedException;
import io.github.olszewskif.fridge.inventory.domain.exception.UserNotFoundException;
import io.github.olszewskif.fridge.shared.domain.AggregateRoot;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.time.Instant;
import java.util.*;

public final class Fridge extends AggregateRoot<FridgeId> {

    private static final int MAX_USERS = 8;

    private String name;
    private final Instant createdAt;
    private final UserId createdBy;
    private final List<FridgeItem> items;
    private final Set<UserId> userIds;

    private Fridge(FridgeId id, String name, UserId createdBy) {
        this(id, name, Instant.now(), createdBy, new ArrayList<>(), new HashSet<>());
    }

    private Fridge(FridgeId id, String name, Instant createdAt, UserId createdBy,
                   List<FridgeItem> fridgeItems, Set<UserId> userIds) {
        super(id);
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank!");
        }
        this.name = name;
        this.createdAt = Objects.requireNonNull(createdAt, "Creation time cannot be null!");
        this.createdBy = Objects.requireNonNull(createdBy, "User ID cannot be null!");
        this.items = fridgeItems == null ? new ArrayList<>() : new ArrayList<>(fridgeItems);
        this.userIds = userIds == null ? new HashSet<>() : new HashSet<>(userIds);
    }

    public static Fridge create(String name, UserId createdBy) {
        return new Fridge(FridgeId.generate(), name, createdBy);
    }

    public static Fridge restore(FridgeId id, String name, Instant createdAt, UserId createdBy,
                                 List<FridgeItem> fridgeItems, Set<UserId> userIds) {
        return new Fridge(id, name, createdAt, createdBy, fridgeItems, userIds);
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

    @Nonnull public Set<UserId> getUserIds() {
        return Set.copyOf(this.userIds);
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

    public void addUser(UserId userId) {
        if (userIds.contains(userId)) {
            throw new UserAlreadyJoinedException(this.id, userId);
        }
        if (userIds.size() >= MAX_USERS) {
            throw new UserLimitReachedException(this.id, MAX_USERS);
        }
        userIds.add(userId);
    }

    public void removeUser(UserId userId) {
        boolean removed = this.userIds.removeIf(i -> i.equals(userId));
        if (!removed) {
            throw new UserNotFoundException(this.id, userId);
        }
    }

}
