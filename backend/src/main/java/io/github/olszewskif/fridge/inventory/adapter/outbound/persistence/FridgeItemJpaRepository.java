package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FridgeItemJpaRepository extends JpaRepository<FridgeItemJpaEntity, UUID>,
        JpaSpecificationExecutor<FridgeItemJpaEntity> {
    Page<FridgeItemJpaEntity> findAllByFridgeId(UUID fridgeId, Pageable pageable);
}
