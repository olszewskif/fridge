package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence;

import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.JpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "fridges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FridgeJpaEntity extends JpaEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private UUID createdBy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fridge_id", nullable = false)
    private List<FridgeItemJpaEntity> items = new ArrayList<>();
}
