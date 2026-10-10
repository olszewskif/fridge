package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

@Repository
public interface FridgeJpaRepository extends JpaRepository<FridgeJpaEntity, UUID>, JpaSpecificationExecutor<FridgeJpaEntity> {
}
