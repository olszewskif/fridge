package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence;

import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.JpaEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "category_aliases", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "alias")
    private Set<String> aliases = new HashSet<>();

}
