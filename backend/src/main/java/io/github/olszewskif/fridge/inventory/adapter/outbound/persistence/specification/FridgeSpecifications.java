package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.specification;

import io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.FridgeJpaEntity;
import io.github.olszewskif.fridge.inventory.domain.FridgeFilterQuery;
import io.github.olszewskif.fridge.inventory.domain.UserId;
import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.specification.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.UUID;

@Component
public class FridgeSpecifications implements SpecificationBuilder<FridgeJpaEntity, FridgeFilterQuery> {

    private static Specification<FridgeJpaEntity> bySearchTerm(String search) {
        if (search == null || search.isBlank()) return SpecificationBuilder.getEmpty();

        String pattern = "%" + search.toLowerCase(Locale.ROOT) + "%";
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), pattern);
    }

    private static Specification<FridgeJpaEntity> byOwnerId(UserId ownerId) {
        if (ownerId == null) return SpecificationBuilder.getEmpty();

        return (root, query, cb) ->
                cb.equal(root.get("createdBy"), ownerId.getValue());
    }

    @Override
    public Specification<FridgeJpaEntity> build(FridgeFilterQuery filter) {
        if (filter == null) return SpecificationBuilder.getEmpty();

        return Specification.where(bySearchTerm(filter.search()))
                .and(byOwnerId(filter.ownerId()));
    }
}
