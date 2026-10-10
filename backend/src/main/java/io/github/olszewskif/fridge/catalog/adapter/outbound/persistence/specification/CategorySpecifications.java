package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.specification;

import io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.CategoryJpaEntity;
import io.github.olszewskif.fridge.catalog.domain.CategoryFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.specification.SpecificationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class CategorySpecifications implements SpecificationBuilder<CategoryJpaEntity, CategoryFilterQuery> {

    private static Specification<CategoryJpaEntity> bySearchTerm(String search) {
        if (search == null || search.isBlank()) return SpecificationBuilder.getEmpty();

        String pattern = "%" + search.toLowerCase(Locale.ROOT) + "%";
        return (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), pattern);
    }

    private static Specification<CategoryJpaEntity> byParentId(CategoryId parentId) {
        if (parentId == null || parentId.getValue() == null) return SpecificationBuilder.getEmpty();

        return (root, query, cb) ->
                cb.equal(root.get("parentId"), parentId.getValue());
    }

    private static Specification<CategoryJpaEntity> isRoot(Boolean isRoot) {
        if (isRoot == null) return SpecificationBuilder.getEmpty();
        return (root, query, cb) ->
                isRoot ? cb.isNull(root.get("parentId"))
                       : cb.isNotNull(root.get("parentId"));
    }


    @Override
    public Specification<CategoryJpaEntity> build(CategoryFilterQuery criteria) {
        if (criteria == null) return SpecificationBuilder.getEmpty();

        return Specification.where(bySearchTerm(criteria.search()))
                .and(byParentId(criteria.parentId()))
                .and(isRoot(criteria.isRoot()));
    }
}
