package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence;

import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.JpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryJpaEntity extends JpaEntity {

    @Column(nullable = false)
    private String name;

    private UUID parentId;

}
