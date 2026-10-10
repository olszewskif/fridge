package io.github.olszewskif.fridge.shared.adapter.outbound.persistence.specification;

import io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.ProductJpaEntity;
import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.JpaEntity;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<E extends JpaEntity, C> {
    Specification<E> build(C criteria);

    static <E extends JpaEntity> Specification<E> getEmpty() {
        return (root, query, cb) -> cb.conjunction();
    }
}
