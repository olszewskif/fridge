package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.specification;

import io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.FridgeItemJpaEntity;
import io.github.olszewskif.fridge.inventory.application.dto.ResolvedFridgeItemsFilterQuery;
import io.github.olszewskif.fridge.inventory.domain.*;
import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.specification.SpecificationBuilder;
import io.github.olszewskif.fridge.shared.domain.DomainId;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class FridgeItemSpecifications implements SpecificationBuilder<FridgeItemJpaEntity,
        ResolvedFridgeItemsFilterQuery> {

    private static Specification<FridgeItemJpaEntity> byFridgeId(FridgeId fridgeId) {
        return (root, query, cb) ->
                cb.equal(root.get("fridgeId"), fridgeId.getValue());
    }
    private static Specification<FridgeItemJpaEntity> byIsExpired(Boolean isExpired) {
        if (isExpired == null) return SpecificationBuilder.getEmpty();

        return (root, query, cb) ->
                isExpired ? cb.lessThan(root.get("expiryDate"), cb.localDate())
                          : cb.greaterThan(root.get("expiryDate"), cb.localDate());
    }

    private static Specification<FridgeItemJpaEntity> byExpiresInDays(Integer days) {
        if (days == null) return SpecificationBuilder.getEmpty();

        LocalDate date = LocalDate.now().plusDays(days);
        return (root, query, cb) ->
                cb.lessThan(root.get("expiryDate"), date);
    }

    private static Specification<FridgeItemJpaEntity> byResolvedProductIds(Set<ProductId> productIds) {
        if (productIds == null || productIds.isEmpty()) return SpecificationBuilder.getEmpty();

        Set<UUID> uuids = productIds.stream().map(DomainId::getValue).collect(Collectors.toSet());
        return (root, query, cb) ->
                root.get("productId").in(uuids);
    }

    private static Specification<FridgeItemJpaEntity> byProductId(ProductId productId) {
        if (productId == null) return SpecificationBuilder.getEmpty();

        return (root, query, cb) ->
                cb.equal(root.get("productId"), productId.getValue());
    }

    private static Specification<FridgeItemJpaEntity> byAddedBy(UserId userId) {
        if (userId == null) return SpecificationBuilder.getEmpty();

        return (root, query, cb) ->
                cb.equal(root.get("addedBy"), userId.getValue());
    }

    @Override
    public Specification<FridgeItemJpaEntity> build(ResolvedFridgeItemsFilterQuery criteria) {
        if (criteria == null) return SpecificationBuilder.getEmpty();

        return Specification.where(byFridgeId(criteria.fridgeId()))
                .and(byIsExpired(criteria.isExpired()))
                .and(byExpiresInDays(criteria.expiresInDays()))
                .and(byAddedBy(criteria.addedBy()))
                .and(byResolvedProductIds(criteria.resolvedProductIds()))
                .and(byProductId(criteria.productId()));
    }
}
