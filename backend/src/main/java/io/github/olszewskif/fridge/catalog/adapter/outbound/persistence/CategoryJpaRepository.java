package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, UUID>,
        JpaSpecificationExecutor<CategoryJpaEntity> {
    Set<CategoryJpaEntity> findByNameInIgnoreCase(Set<String> names);
}
