package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence;

import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.JpaEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "fridge_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FridgeItemJpaEntity extends JpaEntity {

    @Column(name = "fridge_id", nullable = false, insertable = false, updatable = false)
    private UUID fridgeId;

    @Column(nullable = false)
    private UUID productId;

    @Column(nullable = false)
    private UUID addedBy;

    @Column(nullable = false)
    private Instant addedAt;

    @Column(nullable = false)
    @Min(1)
    private int quantity;

    private LocalDate expiryDate;
}
