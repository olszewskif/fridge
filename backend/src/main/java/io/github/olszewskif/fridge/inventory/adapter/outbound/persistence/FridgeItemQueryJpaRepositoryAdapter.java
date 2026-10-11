package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence;

import io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.mapper.FridgeItemJpaMapper;
import io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.specification.FridgeItemSpecifications;
import io.github.olszewskif.fridge.inventory.application.dto.FridgeItemResult;
import io.github.olszewskif.fridge.inventory.application.dto.GetFridgeItemQuery;
import io.github.olszewskif.fridge.inventory.application.dto.ResolvedFridgeItemsFilterQuery;
import io.github.olszewskif.fridge.inventory.application.port.FridgeItemQueryRepository;
import io.github.olszewskif.fridge.inventory.domain.*;
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
public class FridgeItemQueryJpaRepositoryAdapter implements FridgeItemQueryRepository {

    private final FridgeItemJpaRepository jpaRepository;
    private final CommonJpaMapper commonJpaMapper;
    private final FridgeItemJpaMapper fridgeItemJpaMapper;
    private final FridgeItemSpecifications specifications;

    @Override
    public Optional<FridgeItemResult> findById(GetFridgeItemQuery query) {
        Optional<FridgeItemJpaEntity> item = jpaRepository.findById(query.fridgeItemId().getValue());
        return item.map(fridgeItemJpaMapper::toResult);
    }

    @Override
    public PageResult<FridgeItemResult> findAll(ResolvedFridgeItemsFilterQuery filters,
                                                PageQuery pageQuery) {
        Specification<FridgeItemJpaEntity> spec = specifications.build(filters);
        Pageable pageable = commonJpaMapper.toPageable(pageQuery);
        Page<FridgeItemJpaEntity> page = jpaRepository.findAll(spec, pageable);

        return commonJpaMapper.toPageResult(page)
                .map(fridgeItemJpaMapper::toResult);
    }
}
