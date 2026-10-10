package io.github.olszewskif.fridge.inventory.application.service;

import io.github.olszewskif.fridge.inventory.application.dto.*;
import io.github.olszewskif.fridge.inventory.application.mapper.FridgeItemAppMapper;
import io.github.olszewskif.fridge.inventory.application.port.FridgeQueryPort;
import io.github.olszewskif.fridge.inventory.application.usecase.FridgeItemQueryUseCase;
import io.github.olszewskif.fridge.inventory.domain.Fridge;
import io.github.olszewskif.fridge.inventory.domain.FridgeId;
import io.github.olszewskif.fridge.inventory.domain.FridgeItem;
import io.github.olszewskif.fridge.inventory.domain.UserId;
import io.github.olszewskif.fridge.inventory.domain.exception.FridgeItemNotFoundException;
import io.github.olszewskif.fridge.inventory.domain.exception.FridgeNotFoundException;
import io.github.olszewskif.fridge.inventory.domain.port.FridgeRepository;
import io.github.olszewskif.fridge.shared.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FridgeItemQueryService implements FridgeItemQueryUseCase {

    private final FridgeQueryPort fridgeQueryPort;

    @Override
    public FridgeItemResult getItem(GetFridgeItemQuery query) {
        return fridgeQueryPort.findById(query)
                .orElseThrow(() -> new FridgeItemNotFoundException(query.fridgeItemId()));
    }

    @Override
    public PageResult<FridgeItemResult> getItems(GetFridgeItemsQuery query) {
        return fridgeQueryPort.findAllByFridgeId(query);
    }


}
