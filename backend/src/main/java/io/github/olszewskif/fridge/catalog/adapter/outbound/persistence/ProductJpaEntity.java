package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence;

import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.JpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductJpaEntity extends JpaEntity {

    @Column(nullable = false)
    private String name;

    private String brand;

    @Column(nullable = false, unique = true)
    private String barcode;

    private Double amount;

    private String unit;

    private String imageUrl;

    @ElementCollection
    @CollectionTable(
            name = "product_categories",
            joinColumns = @JoinColumn(name = "product_id")
    )
    @Column(name = "category_id", nullable = false)
    private Set<UUID> categoryIds = new HashSet<>();
}

// e54e9114-8c6e-461d-848b-a3d8a0799475
// f4347160-f80a-4f7a-93c6-583975b21bf4