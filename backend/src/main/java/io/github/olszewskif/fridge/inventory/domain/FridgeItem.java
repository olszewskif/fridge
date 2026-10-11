package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.shared.domain.Entity;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.time.Instant;
import java.util.Objects;

public final class FridgeItem extends Entity<FridgeItemId> {

    private final ProductId productId;
    private final UserId addedBy;
    private final Instant addedAt;
    private Quantity quantity;
    private ExpiryDate expiryDate;

    private FridgeItem(FridgeItemId id, ProductId productId, UserId addedBy,
                       Instant addedAt, Quantity quantity) {
        this(id, productId, addedBy, addedAt, quantity, null);
    }

    private FridgeItem(FridgeItemId id, ProductId productId, UserId addedBy,
                       Instant addedAt, Quantity quantity, @Nullable ExpiryDate expiryDate) {
        super(id);
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null!");
        this.addedBy = Objects.requireNonNull(addedBy, "User ID cannot be null!");
        this.addedAt = Objects.requireNonNull(addedAt, "Creation time cannot be null!");
        this.quantity = Objects.requireNonNull(quantity, "Quantity cannot be null!");
        this.expiryDate = expiryDate;
    }

    static FridgeItem create(ProductId productId, UserId addedBy, Quantity quantity) {
        return new FridgeItem(
                FridgeItemId.generate(),
                productId,
                addedBy,
                Instant.now(),
                quantity
        );
    }

    static FridgeItem create(ProductId productId, UserId addedBy, Quantity quantity,
                             @Nullable ExpiryDate expiryDate) {
        return new FridgeItem(
                FridgeItemId.generate(),
                productId,
                addedBy,
                Instant.now(),
                quantity,
                expiryDate
        );
    }

    public static FridgeItem restore(FridgeItemId id, ProductId productId, UserId addedBy,
                              Instant addedAt, Quantity quantity, ExpiryDate expiryDate) {
        return new FridgeItem(
                id,
                productId,
                addedBy,
                addedAt,
                quantity,
                expiryDate
        );
    }

    @Nonnull public ProductId getProductId() {
        return productId;
    }

    @Nonnull public UserId getAddedBy() {
        return addedBy;
    }

    @Nonnull public Instant getAddedAt() {
        return addedAt;
    }

    @Nonnull public Quantity getQuantity() {
        return quantity;
    }

    @Nullable public ExpiryDate getExpiryDate() {
        return expiryDate;
    }

    void changeQuantity(@Nonnull Quantity quantity) {
        this.quantity = Objects.requireNonNull(quantity, "Quantity cannot be null!");
    }

    void changeExpiryDate(@Nullable ExpiryDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
