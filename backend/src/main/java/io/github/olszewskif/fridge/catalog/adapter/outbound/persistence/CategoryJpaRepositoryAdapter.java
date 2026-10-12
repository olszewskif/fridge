package io.github.olszewskif.fridge.catalog.adapter.outbound.persistence;

import io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.mapper.CategoryJpaMapper;
import io.github.olszewskif.fridge.catalog.adapter.outbound.persistence.specification.CategorySpecifications;
import io.github.olszewskif.fridge.catalog.domain.Category;
import io.github.olszewskif.fridge.catalog.domain.CategoryFilterQuery;
import io.github.olszewskif.fridge.catalog.domain.CategoryId;
import io.github.olszewskif.fridge.catalog.domain.port.CategoryRepository;
import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.mapper.CommonJpaMapper;
import io.github.olszewskif.fridge.shared.domain.DomainId;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryJpaRepositoryAdapter implements CategoryRepository {

    private final CategoryJpaRepository jpaRepository;
    private final CategoryJpaMapper mapper;
    private final CommonJpaMapper commonJpaMapper;
    private final CategorySpecifications specifications;

    @Override
    public Optional<Category> findById(CategoryId id) {
        return jpaRepository.findById(id.getValue()).map(mapper::toDomainEntity);
    }

    @Override
    public boolean existsById(CategoryId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public PageResult<Category> findAll(CategoryFilterQuery filters, PageQuery pageQuery) {
        Pageable pageable = commonJpaMapper.toPageable(pageQuery);
        Specification<CategoryJpaEntity> spec = specifications.build(filters);
        Page<CategoryJpaEntity> page = jpaRepository.findAll(spec, pageable);
        return commonJpaMapper.toPageResult(page)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Set<Category> findAllById(Set<CategoryId> ids) {
        List<UUID> uuidIds = ids.stream().map(DomainId::getValue).toList();
        return jpaRepository.findAllById(uuidIds).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toSet());
    }

    @Override
    public Category save(Category category) {
        CategoryJpaEntity entity = jpaRepository.findById(category.getId().getValue())
                .map(existing -> {
                    mapper.updateJpaEntity(category, existing);
                    return existing;
                })
                .orElseGet(() -> mapper.toJpaEntity(category));
        return mapper.toDomainEntity(jpaRepository.save(entity));
    }

    @Override
    public void delete(CategoryId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public Set<Category> findAllByNames(Set<String> names) {
        return jpaRepository.findByNameInIgnoreCase(names).stream()
                .map(mapper::toDomainEntity).collect(Collectors.toSet());
    }

    @Override
    public Set<Category> findByNameOrAliasIn(Set<String> terms) {
        if (terms == null || terms.isEmpty()) {
            return Collections.emptySet();
        }
        return jpaRepository.findByNameOrAliasInIgnoreCase(terms).stream()
                .map(mapper::toDomainEntity)
                .collect(Collectors.toSet());
    }

}
