package io.github.olszewskif.fridge.inventory.adapter.outbound.persistence;

import io.github.olszewskif.fridge.inventory.adapter.outbound.persistence.mapper.FridgeItemJpaMapper;
import io.github.olszewskif.fridge.inventory.application.dto.FridgeItemResult;
import io.github.olszewskif.fridge.inventory.application.dto.GetFridgeItemQuery;
import io.github.olszewskif.fridge.inventory.application.dto.GetFridgeItemsQuery;
import io.github.olszewskif.fridge.inventory.application.port.FridgeQueryPort;
import io.github.olszewskif.fridge.inventory.domain.*;
import io.github.olszewskif.fridge.shared.adapter.outbound.persistence.mapper.CommonJpaMapper;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FridgeJpaQueryAdapter implements FridgeQueryPort {

    private final FridgeItemJpaRepository jpaRepository;
    private final CommonJpaMapper commonJpaMapper;
    private final FridgeItemJpaMapper fridgeItemJpaMapper;

    @Override
    public PageResult<FridgeItemResult> findAllByFridgeId(GetFridgeItemsQuery query) {
        Page<FridgeItemJpaEntity> page = jpaRepository.findAllByFridgeId(
                query.fridgeId().getValue(),
                commonJpaMapper.toPageable(query.pageQuery()));

        return commonJpaMapper.toPageResult(page)
                .map(fridgeItemJpaMapper::toResult);
    }

    @Override
    public Optional<FridgeItemResult> findById(GetFridgeItemQuery query) {
        Optional<FridgeItemJpaEntity> item = jpaRepository.findById(query.fridgeItemId().getValue());
        return item.map(fridgeItemJpaMapper::toResult);
    }
}
