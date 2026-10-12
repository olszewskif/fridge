package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.mapper;

import io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.CategoryJpaEntity;
import io.github.olszewskif.fridge.catalog.domain.Alias;
import io.github.olszewskif.fridge.catalog.domain.Category;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public final class CategoryJpaMapper {

    public CategoryJpaEntity toJpaEntity(Category category) {
        CategoryJpaEntity entity = new CategoryJpaEntity();
        entity.setId(category.getId().getValue());
        updateJpaEntity(category, entity);
        return entity;
    }

    public void updateJpaEntity(Category category, CategoryJpaEntity entity) {
        entity.setName(category.getName());
        entity.setParentId(category.getParentId() == null ? null : category.getParentId().getValue());
        entity.setAliases(category.getAliases().stream()
                .map(Alias::value)
                .collect(Collectors.toSet()));
    }

    public Category toDomainEntity(CategoryJpaEntity entity) {
        Set<Alias> aliases = entity.getAliases() == null ? Collections.emptySet()
                : entity.getAliases().stream().map(Alias::of).collect(Collectors.toSet());

        return Category.restore(
                CategoryId.of(entity.getId()),
                entity.getName(),
                entity.getParentId() == null ? null : CategoryId.of(entity.getParentId()),
                aliases
        );
    }
}
