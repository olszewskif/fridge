package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence;

import io.github.olszewskif.fridge.inventory.domain.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface FridgeItemJpaRepository extends JpaRepository<FridgeItemJpaEntity, UUID> {
    Page<FridgeItemJpaEntity> findAllByFridgeId(UUID fridgeId, Pageable pageable);
}
