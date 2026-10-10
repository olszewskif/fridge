package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductJpaRepository extends JpaRepository<ProductJpaEntity, UUID>,
        JpaSpecificationExecutor<ProductJpaEntity> {
    Optional<ProductJpaEntity> findByBarcode(String barcode);
}
