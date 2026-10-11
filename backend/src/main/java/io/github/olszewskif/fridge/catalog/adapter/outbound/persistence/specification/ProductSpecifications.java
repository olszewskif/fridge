package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.specification;

import io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.ProductJpaEntity;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.ProductFilterQuery;
import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.specification.SpecificationBuilder;
import io.github.olszewskif.fridge.shared.domain.DomainId;
import jakarta.annotation.Nullable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProductSpecifications implements SpecificationBuilder<ProductJpaEntity, ProductFilterQuery> {

    private static Specification<ProductJpaEntity> byCategories(@Nullable Set<CategoryId> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) return SpecificationBuilder.getEmpty();

        Set<UUID> uuids = categoryIds.stream().map(DomainId::getValue)
                .collect(Collectors.toSet());

        return (root, query, cb) -> {
            query.distinct(true);
            return root.join("categoryIds").in(uuids);
        };
    }

    private static Specification<ProductJpaEntity> bySearchTerm(@Nullable String search) {
        if (search == null || search.isBlank()) return SpecificationBuilder.getEmpty();

        String searchLower = search.toLowerCase(Locale.ROOT);

        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), "%" + searchLower + "%"),
                cb.like(cb.lower(root.get("brand")), "%" + searchLower + "%"));
    }

    @Override
    public Specification<ProductJpaEntity> build(ProductFilterQuery criteria) {
        if (criteria == null) return SpecificationBuilder.getEmpty();

        return Specification.where(byCategories(criteria.categoryIds()))
                .and(bySearchTerm(criteria.search()));
    }
}
