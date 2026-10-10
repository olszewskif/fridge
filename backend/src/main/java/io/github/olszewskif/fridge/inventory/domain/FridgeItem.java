package io.github.olszewskif.fridge.inventory.domain;

import io.github.olszewskif.fridge.shared.domain.Entity;
import jakarta.annotation.Nullable;
import lombok.Getter;

import java.time.Instant;
import java.util.Objects;

@Getter
public final class FridgeItem extends Entity<FridgeItemId> {

    private final ProductId productId;
    private final UserId addedBy;
    private final Instant addedAt;
    private Amount amount;
    private ExpiryDate expiryDate;

    private FridgeItem(FridgeItemId id, ProductId productId, UserId addedBy,
                       Instant addedAt, Amount amount) {
        this(id, productId, addedBy, addedAt, amount, null);
    }

    private FridgeItem(FridgeItemId id, ProductId productId, UserId addedBy,
                       Instant addedAt, Amount amount, @Nullable ExpiryDate expiryDate) {
        super(id);
        this.productId = Objects.requireNonNull(productId, "Product ID cannot be null!");
        this.addedBy = Objects.requireNonNull(addedBy, "User ID cannot be null!");
        this.addedAt = Objects.requireNonNull(addedAt, "Creation time cannot be null!");
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null!");
        this.expiryDate = expiryDate;
    }

    static FridgeItem create(ProductId productId, UserId addedBy, Amount amount) {
        return new FridgeItem(
                FridgeItemId.generate(),
                productId,
                addedBy,
                Instant.now(),
                amount
        );
    }

    static FridgeItem create(ProductId productId, UserId addedBy, Amount amount,
                             ExpiryDate expiryDate) {
        return new FridgeItem(
                FridgeItemId.generate(),
                productId,
                addedBy,
                Instant.now(),
                amount,
                expiryDate
        );
    }

    public static FridgeItem restore(FridgeItemId id, ProductId productId, UserId addedBy,
                              Instant addedAt, Amount amount, ExpiryDate expiryDate) {
        return new FridgeItem(
                id,
                productId,
                addedBy,
                addedAt,
                amount,
                expiryDate
        );
    }

    void changeAmount(Amount amount) {
        this.amount = Objects.requireNonNull(amount, "Amount cannot be null!");
    }

    void changeExpiryDate(@Nullable ExpiryDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
