package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence;

import io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.mapper.FridgeJpaMapper;
import io.github.olszewskif.fridge.inventory.domain.Fridge;
import io.github.olszewskif.fridge.inventory.domain.FridgeFilterQuery;
import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.port.FridgeRepository;
import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.mapper.CommonJpaMapper;
import io.github.olszewskif.fridge.shared.domain.PageQuery;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FridgeJpaRepositoryAdapter implements FridgeRepository {

    private final FridgeJpaRepository jpaRepository;
    private final FridgeJpaMapper mapper;
    private final CommonJpaMapper commonJpaMapper;

    private final io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.specification.FridgeSpecifications specifications;

    @Override
    public Optional<Fridge> findById(FridgeId id) {
        return jpaRepository.findById(id.getValue()).map(mapper::toDomainEntity);
    }

    @Override
    public boolean existsById(FridgeId id) {
        return jpaRepository.existsById(id.getValue());
    }

    @Override
    public PageResult<Fridge> findAll(FridgeFilterQuery filters, PageQuery pageQuery) {
        Pageable pageable = commonJpaMapper.toPageable(pageQuery);
        Specification<FridgeJpaEntity> spec = specifications.build(filters);
        Page<FridgeJpaEntity> page = jpaRepository.findAll(spec, pageable);
        return commonJpaMapper.toPageResult(page)
                .map(mapper::toDomainEntity);
    }

    @Override
    public Fridge save(Fridge fridge) {
        FridgeJpaEntity entity = jpaRepository.findById(fridge.getId().getValue())
                .map(existing -> {
                    mapper.updateJpaEntity(fridge, existing);
                    return existing;
                })
                .orElseGet(() -> mapper.toJpaEntity(fridge));
        return mapper.toDomainEntity(jpaRepository.save(entity));
    }

    @Override
    public void delete(FridgeId id) {
        jpaRepository.deleteById(id.getValue());
    }

}
